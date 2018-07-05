package com.lspooo.plugin.location;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lspooo.plugin.common.common.menu.ActionMenuItem;
import com.lspooo.plugin.common.tools.BackwardSupportUtil;
import com.lspooo.plugin.common.tools.ResourceHelper;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.location.adapter.PoiListAdapter;
import com.lspooo.plugin.location.bean.NearByLocation;
import com.lspooo.plugin.location.presenter.LocationPresenter;
import com.lspooo.plugin.location.presenter.LocationView;
import com.lspooo.plugin.location.view.CustomMapView;
import com.lspooo.plugin.location.view.LoadMoreListView;
import com.lspooo.plugin.location.view.PickPoi;
import com.lspooo.plugin.location.view.LocationPoint;
import com.lspooo.plugin.location.view.ViewTranslateAnimation;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.Circle;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.List;


public class SendLocationUI extends CommonActivity<LocationView, LocationPresenter> implements TencentLocationListener, LocationView {

    private CustomMapView mCustomMapView;
    private LoadMoreListView mListView;
    private PoiListAdapter mPoiAdapter;
    private RelativeLayout mInitProcessDialog;
    private RelativeLayout mPoiLayout;
    private ImageView mLocationBtn;
    private FrameLayout mMapFrameLayout;
    private FrameLayout mControllerLayout;
    private LocationPoint mLocationPoint;
    private PickPoi mPickPoi;

    private TencentLocationManager locationManager;
    private TencentLocationRequest locationRequest;
    private TencentMap mTencentMap;
    private Circle accuracy;
    private Marker mLocation;

    private boolean mOnAnimationEnd = true;
    private boolean mMapAnimation = false;
    private boolean mStartRequestLocation = false;

    private int markToTopValue;
    private int mTopMax;
    private int mTopMin;
    private NearByLocation mNearByLocation;
    private double mMapCenterX = -1000.0D;
    private double mMapCenterY = -1000.0D;
    private String mProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = TencentLocationManager.getInstance(this);
        locationRequest = TencentLocationRequest.create();
        locationRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        initTopBarView();
        initMapView(savedInstanceState);
        initPoiListView();
        startRequestLocationUpdates();
    }

    private void initTopBarView() {
        setActionBarTitle(getString(R.string.location_info));
        setActionMenuItem(0, R.drawable.ic_search_white, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(SendLocationUI.this, SearchLocationUI.class);
                intent.putExtra("province", mProvince);
                startActivityForResult(intent, 0x1);
                SendLocationUI.this.overridePendingTransition(0, 0);
                return true;
            }
        });
        setActionMenuItem(1, getString(R.string.app_send), new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                }, ActionMenuItem.ActionType.BUTTON);
        setSingleActionMenuItemEnabled(1, false);
    }

    private void initMapView(Bundle savedInstanceState) {
        mMapFrameLayout = (FrameLayout) findViewById(R.id.map_content);
        mCustomMapView = (CustomMapView) findViewById(R.id.g_mapView);
        mCustomMapView.onCreate(savedInstanceState);
        mTencentMap = mCustomMapView.getMap();
        mCustomMapView.setMapViewOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startLocationPoiPick();
                    mMapCenterX = mTencentMap.getMapCenter().getLatitude();
                    mMapCenterY = mTencentMap.getMapCenter().getLongitude();
                    mPresenter.startRequestPoiMessage(SendLocationUI.this, (float) mMapCenterX,
                            (float) mMapCenterY, getString(R.string.location_selected));
                }
                return false;
            }
        });
        mLocationBtn = (ImageView) findViewById(R.id.locate_to_my_position);
        mLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRequestLocationUpdates();
                if (mLocationPoint != null) {
                    mLocationPoint.remove();
                }
            }
        });
        mControllerLayout = (FrameLayout) findViewById(R.id.control_id);
        mLocationPoint = new LocationPoint(this, mCustomMapView);
        mPickPoi = new PickPoi(this);
        mPickPoi.setLocationArrow(R.drawable.location_on);
        mControllerLayout.addView(mPickPoi);
        mTopMax = BackwardSupportUtil.px2dip(this, 280.0F);
        mTopMin = BackwardSupportUtil.px2dip(this, 150.0F);
    }

    private void initPoiListView() {

        mPoiLayout = (RelativeLayout) findViewById(R.id.poi_layout);
        mInitProcessDialog = (RelativeLayout) findViewById(R.id.load);
        mListView = (LoadMoreListView) findViewById(R.id.poi_list);
        mListView.hideFooterViewLoading();
        mListView.setOnTouchListener(new View.OnTouchListener() {

            private float mRawY;
            private short mUp = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 动画没有完成，则不处理
                if (!mOnAnimationEnd) {
                    return true;
                }
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mRawY = event.getRawY();
                        mMapAnimation = false;
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        if (mMapAnimation) {
                            mListView.setSelection(0);
                        }

                        float distance = mRawY - event.getRawY();
                        if (Math.abs(distance) < BackwardSupportUtil.px2dip(SendLocationUI.this, 20.0F)) {
                            mUp = 0;
                        } else if (distance > 0.0F) {
                            mUp = 1;
                        } else {
                            mUp = -1;
                        }
                        if ((markTopMargin() > mTopMin || mUp != 1) &&
                                (mListView.getScroll2Top() || mUp != -1 || markTopMargin() >= mTopMax) &&
                                (mUp != -1 || markTopMargin() < mTopMax)) {
                            if (mOnAnimationEnd && mUp != 0) {
                                if (this.mUp == 1) {
                                    doMapUpOrDown(true);
                                } else {
                                    doMapUpOrDown(false);
                                }
                                return false;
                            }
                            return true;
                        }
                        return false;
                    case MotionEvent.ACTION_UP:
                        mMapAnimation = false;
                        return false;
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearByLocation location = (NearByLocation) mPoiAdapter.getItem(position);
                mCustomMapView.getMapController().animateTo(new GeoPoint((int) (1000000.0D * location.location.lat), (int) (1000000.0D * location.location.lng)));
                mNearByLocation = location;
                if (mLocationPoint.mInitPoint) {
                    mLocationPoint.remove();
                }
                mPoiAdapter.setSelectedPosition(position);
                mLocationPoint.mInitPoint = true;
                mLocationPoint.mCustomMapView.addView(mLocationPoint, location.location.lat, location.location.lng);
            }
        });
        mPoiAdapter = new PoiListAdapter(this);
        mListView.setAdapter(mPoiAdapter);
        mListView.initOnScrollListener();

        int heightPixels = ResourceHelper.getHeightPixels(this);
        int statusBarHeight = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarHeight > 0) {
            statusBarHeight = getApplicationContext().getResources().getDimensionPixelSize(statusBarHeight);
        }
        int actionBarHeight = ResourceHelper.getDimensionPixelSize(this, R.dimen.action_bar_height);
        int height = heightPixels - mTopMin - statusBarHeight - actionBarHeight;
        if (heightPixels > ResourceHelper.getDimensionPixelSize(this, R.dimen.ytx_map_list_height)) {
            ViewGroup.LayoutParams layoutParams = mPoiLayout.getLayoutParams();
            layoutParams.height = height;
            mPoiLayout.setLayoutParams(layoutParams);
        }
    }

    /**
     * 处理红色标记动画
     */
    public void startLocationPoiPick() {
        if (mPickPoi != null) {
            mPickPoi.startAnimation();
            mMapCenterX = mCustomMapView.getMapCenterX() / 1000000.0D;
            mMapCenterY = mCustomMapView.getMapCenterY() / 1000000.0D;
            mPickPoi.setNewMapCenter(mMapCenterX, mMapCenterY);
        }
    }

    /**
     * 开始定位到当前的位置
     */
    private void startRequestLocationUpdates() {
        mStartRequestLocation = true;
        int error = locationManager.requestLocationUpdates(locationRequest, SendLocationUI.this);
        switch (error) {
            case 0:
                Log.e("location", "成功注册监听器");
                break;
            case 1:
                Log.e("location", "设备缺少使用腾讯定位服务需要的基本条件");
                break;
            case 2:
                Log.e("location", "manifest 中配置的 key 不正确");
                break;
            case 3:
                Log.e("location", "自动加载libtencentloc.so失败");
                break;

            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (i == TencentLocation.ERROR_OK) {
            if ((((mMapCenterX == -1000.0D || mMapCenterY == -1000.0D)) || (mStartRequestLocation))) {
                mMapCenterX = tencentLocation.getLatitude();
                mMapCenterY = tencentLocation.getLongitude();
                mProvince = tencentLocation.getProvince();
                setLocationByPosition(tencentLocation);
            }
        }
    }

    /**
     * 根据当前的地理位置定位
     * @param tencentLocation 开始定位
     */
    private void setLocationByPosition(TencentLocation tencentLocation) {
        LatLng latLng = new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude());
        if (mLocation == null) {
            mLocation = mTencentMap.addMarker(new MarkerOptions().
                    position(latLng).
                    icon(BitmapDescriptorFactory.fromResource(R.raw.navigation))
                    .anchor(0.5f, 0.5f));
        }
        if (accuracy == null) {
            accuracy = mTencentMap.addCircle(new CircleOptions().
                    center(latLng).visible(false).
                    radius((double) tencentLocation.getAccuracy()).
                    fillColor(0x440000ff).
                    strokeWidth(0f));
        }
        mLocation.setPosition(latLng);
        accuracy.setCenter(latLng);
        accuracy.setRadius(tencentLocation.getAccuracy());
        mTencentMap.animateTo(latLng);
        mTencentMap.setZoom(16);
        mPresenter.startRequestPoiMessage(this, (float) tencentLocation.getLatitude(),
                (float) tencentLocation.getLongitude(), tencentLocation.getAddress());
        mStartRequestLocation = false;
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {}

    /**
     * 初始化上移动的位置距离
     * @return 距离
     */
    public final int markTopMargin() {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mPoiLayout.getLayoutParams();
        markToTopValue = marginLayoutParams.topMargin;
        return marginLayoutParams.topMargin;
    }

    /**
     * 执行地图伸展或者缩放
     * @param isUp 是否缩放
     */
    public void doMapUpOrDown(final boolean isUp) {
        mOnAnimationEnd = false;
        ViewTranslateAnimation v1;
        ViewTranslateAnimation v2;
        if (isUp) {
            v2 = new ViewTranslateAnimation(-(markTopMargin() - mTopMin));
            v1 = new ViewTranslateAnimation(-(markTopMargin() - mTopMin) / 2);
        } else {
            v2 = new ViewTranslateAnimation(mTopMax - markTopMargin());
            v1 = new ViewTranslateAnimation((mTopMax - markTopMargin()) / 2);
        }
        v2.setDuration(200L);
        v2 = v2.create();
        v2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mOnAnimationEnd = false;
                mMapAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tryUpdateLayout(isUp ? mTopMin : mTopMax);
                mOnAnimationEnd = true;
                mPoiLayout.clearAnimation();
                mLocationBtn.clearAnimation();
                mMapFrameLayout.clearAnimation();
                mListView.clearFocus();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        v2.setView(mPoiLayout).setView(mLocationBtn).start();
        v1.setDuration(200L);
        v1.create().setView(mMapFrameLayout).start();
    }

    /**
     * 通知界面更新
     * @param topMargin 距离
     */
    public void tryUpdateLayout(int topMargin) {
        ((FrameLayout.LayoutParams) mPoiLayout.getLayoutParams()).topMargin = topMargin;
        int dip = BackwardSupportUtil.px2dip(this, 65.0F);
        ((FrameLayout.LayoutParams) mLocationBtn.getLayoutParams()).topMargin = (topMargin - dip);
        dip = (topMargin - this.markToTopValue) / 2;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mMapFrameLayout.getLayoutParams();
        if (topMargin == mTopMin) {
            layoutParams.topMargin = BackwardSupportUtil.px2dip(this, -65.0F);
        } else if (topMargin == mTopMax) {
            layoutParams.topMargin = 0;
        } else {
            layoutParams.topMargin = layoutParams.topMargin + dip;
        }
        this.mMapFrameLayout.requestLayout();
        this.markToTopValue = topMargin;
        this.mPoiLayout.requestLayout();
        this.mLocationBtn.requestLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1) {
            if (data == null) {
                return;
            }
            float lat = data.getFloatExtra("lat", 0);
            float lng = data.getFloatExtra("lng", 0);
            String address = data.getStringExtra("address");
            mCustomMapView.getMapController().animateTo(new GeoPoint((int) (1000000.0D * lat), (int) (1000000.0D * lng)));
            mPresenter.startRequestPoiMessage(this, lat, lng, address);
        }
    }

    @Override
    protected void onResume() {
        mCustomMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mCustomMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mCustomMapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mCustomMapView.onDestroy();
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_location;
    }

    @Override
    public LocationPresenter getPresenter() {
        return new LocationPresenter();
    }

    @Override
    public void onRequestPoiResult(boolean success, NearByLocation location, List<NearByLocation> result) {
        mInitProcessDialog.setVisibility(View.GONE);
        if (success) {
            setSingleActionMenuItemEnabled(1, true);
            mNearByLocation = location;
            mPoiAdapter.clean();
            mPoiAdapter.setCurrentLocation(mNearByLocation);
            mPoiAdapter.setNearBys(result);
        }
    }
}
