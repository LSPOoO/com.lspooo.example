package com.lspooo.plugin.location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.location.view.CustomMapView;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.CancelableCallback;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

/**
 * Created by lspooo on 2018/4/17.
 */

public class LocationUI extends CommonActivity {

    public final static String EXTRA_LATITUDE = "extra_latitude";
    public final static String EXTRA_LONGITUDE = "extra_longitude";
    public final static String EXTRA_ADDRESS = "extra_address";

    private CustomMapView mCustomMapView;
    private TencentMap mTencentMap;
    private TextView addressTv;
    private TencentLocationManager locationManager;
    private TencentLocationRequest locationRequest;


    private double lat = 0;
    private double lng = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = TencentLocationManager.getInstance(this);
        locationRequest = TencentLocationRequest.create();
        locationRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        initMapView(savedInstanceState);
    }

    private void initMapView(Bundle savedInstanceState) {
        mCustomMapView = (CustomMapView) findViewById(R.id.g_mapView);
        mCustomMapView.onCreate(savedInstanceState);
        mTencentMap = mCustomMapView.getMap();
        addressTv = (TextView) findViewById(R.id.addressTv);

        String address = getIntent().getStringExtra(EXTRA_ADDRESS);
        lat = getIntent().getDoubleExtra(EXTRA_LATITUDE, 0f);
        lng = getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0f);
        if (lat != 0 && lng != 0) {
            LatLng latLng = new LatLng(lat, lng);
            mTencentMap.setZoom(16);
            mTencentMap.animateTo(latLng, 100L, new CancelableCallback() {
                @Override
                public void onFinish() {
                    mTencentMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .anchor(0.5f, 0.5f)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_on)));
                }

                @Override
                public void onCancel() {}
            });
            addressTv.setText(address);
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
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_location;
    }
}
