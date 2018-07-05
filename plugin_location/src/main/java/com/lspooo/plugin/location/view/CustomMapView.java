package com.lspooo.plugin.location.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.GroundOverlay;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.Projection;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


/**
 * @author 容联•云通讯
 * @version 5.2.0
 * @since 2016-05-18
 */
public class CustomMapView extends MapView {


    public static final String TAG = "RongXin.RongXinMapView";
    private boolean firstanim = true;
    private IController iController;
    private HashMap<String ,View> tagsView = new HashMap<String ,View>();

    public CustomMapView(Context context) {
        super(context);
        initRongXinMapView();
    }

    public CustomMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initRongXinMapView();
    }

    private void initRongXinMapView() {
        setEnableForeignMap(false);
    }

    private void setEnableForeignMap(boolean enable) {
        try {
            Method method = MapView.class.getDeclaredMethod("setForeignEnabled", Boolean.TYPE);
            method.setAccessible(true);
            method.invoke(this, enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAnimTrackView(View paramView, double paramDouble1, double paramDouble2, String paramString) {
        this.tagsView.put(paramString, paramView);
        addView(paramView, new LayoutParams(-2, -2, new GeoPoint((int) (paramDouble1 * 1000000.0D), (int) (paramDouble2 * 1000000.0D)), 81));
    }

    public void addLocationPin(View paramView) {
        addView(paramView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, (GeoPoint)null, 80));
    }

    public void addNullView(View paramView) {
        addView(paramView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, (GeoPoint)null, 17));
    }

    @Override
    public GroundOverlay addOverlay(Bitmap bitmap, LatLng latLng, LatLng latLng1) {
        return super.addOverlay(bitmap, latLng, latLng1);
    }

    public void addView(View paramView, double paramDouble1, double paramDouble2) {
        addView(paramView, new LayoutParams(-2, -2, new GeoPoint((int) (paramDouble1 * 1000000.0D), (int) (paramDouble2 * 1000000.0D)), 17));
    }

    public void addView(View paramView, double paramDouble1, double paramDouble2, int paramInt) {
        GeoPoint localGeoPoint = new GeoPoint((int) (paramDouble1 * 1000000.0D), (int) (paramDouble2 * 1000000.0D));
        if (paramInt == -2) {
            addView(paramView, new LayoutParams(-2, -2, localGeoPoint, 17));
            return;
        }
        if (paramInt == -1) {
            addView(paramView, new LayoutParams(-1, -1, localGeoPoint, 17));
            return;
        }
        addView(paramView, new LayoutParams(paramInt, paramInt, localGeoPoint, 17));
    }

    public void addView(View paramView, double paramDouble1, double paramDouble2, String paramString) {
        this.tagsView.put(paramString, paramView);
        addView(paramView, paramDouble1, paramDouble2);
    }

    public void clean() {
        this.tagsView.clear();
    }

    public Collection<View> getChilds() {
        return this.tagsView.values();
    }

    public int getMapCenterX() {
        return (int) (getMapCenter().getLatitude() * 1000000.0D);
    }

    public int getMapCenterY() {
        return (int) (getMapCenter().getLongitude() * 1000000.0D);
    }

    public Point getPointByGeoPoint(double latitude, double longitude) {
        Point point = new Point();
        Projection projection = getProjection();
        GeoPoint geoPoint = new GeoPoint((int) (latitude * 1000000.0D), (int) (longitude * 1000000.0D));
        projection.toPixels(geoPoint, point);
        return point;
    }

    public Set<String> getTags() {
        return this.tagsView.keySet();
    }

    public View getViewByItag(String paramString) {
        if (this.tagsView.containsKey(paramString))
            return ((View) this.tagsView.get(paramString));
        return null;
    }

    public int getZoom() {
        return getZoomLevel();
    }

    public View removeViewByTag(String tag) {
        View view = (View) this.tagsView.get(tag);
        removeView(view);
        return view;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        String tag = null;
        for (String s : this.tagsView.keySet()) {
            tag = s;
            View viewByTag = this.tagsView.get(tag);
            if (viewByTag != null && viewByTag.equals(view)) {
                break;
            }
        }
        this.tagsView.remove(tag);
    }

    public void requestMapFocus() {
        requestFocus();
    }

    public void setMapViewOnTouchListener(OnTouchListener paramOnTouchListener) {
        setOnTouchListener(paramOnTouchListener);
    }

    public void updateAnimViewLayout(View paramView, double latitude, double longitude) {
        updateViewLayout(paramView, new LayoutParams(-2, -2, new GeoPoint((int) (latitude * 1000000.0D), (int) (longitude * 1000000.0D)), 81));
    }

    public void updateLocaitonPinLayout(View paramView, double latitude, double longitude) {
        updateViewLayout(paramView, new LayoutParams(-2, -2, new GeoPoint((int) (latitude * 1000000.0D), (int) (longitude * 1000000.0D)), 81));
    }

    public void updateViewLayout(View paramView, double latitude, double longitude) {
        updateViewLayout(paramView, new LayoutParams(-2, -2, new GeoPoint((int) (latitude * 1000000.0D), (int) (longitude * 1000000.0D)), 17));
    }

    public void updateViewLayout(View paramView, double latitude, double longitude, int paramInt) {
        GeoPoint geoPoint = new GeoPoint((int) (latitude * 1000000.0D), (int) (longitude * 1000000.0D));
        if (paramInt == -2) {
            updateViewLayout(paramView, new LayoutParams(-2, -2, geoPoint, 17));
            return;
        }
        if (paramInt == -1) {
            updateViewLayout(paramView, new LayoutParams(-1, -1, geoPoint, 17));
            return;
        }
        updateViewLayout(paramView, new LayoutParams(paramInt, paramInt, geoPoint, 17));
    }

    public void zoomToSpan(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        getController().animateTo(new GeoPoint((int) (1000000.0D * paramDouble1), (int) (1000000.0D * paramDouble2)));
        if ((paramDouble3 == 0.0D) || (paramDouble4 == 0.0D))
            return;
        getController().zoomToSpan((int) (1000000.0D * paramDouble3), (int) (1000000.0D * paramDouble4));
        getController().animateTo(new GeoPoint((int) (1000000.0D * paramDouble1), (int) (1000000.0D * paramDouble2)));
    }


    public interface IController {
        void animateTo(double latitude, double longitude);
        void animateTo(double latitude, double longitude, int paramInt);
        void setCenter(double latitude, double longitude);
        void setZoom(int paramInt);
    }
}
