package com.lspooo.plugin.location.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lspooo.plugin.location.R;


/**
 * @author 容联•云通讯
 * @version 5.2.0
 * @since 2016-05-21
 */
public class PickPoi extends RelativeLayout {

    private Context mContext;
    /**地图移动位置坐标点*/
    private View mPointView;
    /**移动动画*/
    Animation mPointTranslate;

    public PickPoi(Context context) {
        super(context);
        init(context);
    }

    public PickPoi(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PickPoi(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mPointTranslate = AnimationUtils.loadAnimation(this.mContext, R.anim.translate_map);
        mPointView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_ocation_poi_pick, this, true).findViewById(R.id.location_here);
    }

    /**
     * 设置显示移动的图片
     * @param icon 图标
     */
    public void setLocationArrow(int icon) {
        ((ImageView) mPointView).setImageResource(icon);
    }

    /**
     * 设置新的地图中心点
     * @param mapCenterX X-地图中心点
     * @param mapCenterY Y-地图中心点
     */
    public void setNewMapCenter(double mapCenterX, double mapCenterY) {

    }

    public void startAnimation(){
        clearAnimation();
        startAnimation(mPointTranslate);
    }
}
