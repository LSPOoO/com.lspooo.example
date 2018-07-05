package com.lspooo.plugin.location.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lspooo.plugin.location.R;
import com.lspooo.plugin.location.view.CustomMapView;


/**
 * @author 容联•云通讯
 * @version 5.2.0
 * @since 2016-05-22
 */
public class LocationPoint extends RelativeLayout {

    public boolean mInitPoint = false;
    private ImageView mPointImage;
    private Context mContext;
    public CustomMapView mCustomMapView;

    public LocationPoint(Context context , CustomMapView view) {
        super(context);
        mContext = context;
        mCustomMapView = view;
        mPointImage = ((ImageView) View.inflate(this.mContext, R.layout.rn_poi_point, this).findViewById(R.id.point_iv));
    }

    public final void remove() {
        mCustomMapView.removeView(this);
        mInitPoint = false;
    }
}
