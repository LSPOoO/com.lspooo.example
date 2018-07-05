package com.lspooo.plugin.location.view;

/**
 * Created by lspooo on 2018/4/16.
 */

import android.view.View;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * 控件平移动画效果
 */
public class ViewTranslateAnimation extends TranslateAnimation {
    /**
     * 缓存需要平移的View
     */
    private List<View> views = new ArrayList<View>();

    public ViewTranslateAnimation(float toYDelta) {
        super(0.0F, 0.0F, 0.0F, toYDelta);
    }


    /**
     * 注入需要执行动画的控件
     *
     * @param view 动画的控件
     */
    public final ViewTranslateAnimation setView(View view) {
        this.views.add(view);
        return this;
    }

    /**
     * 初始化动画结束标识
     */
    public final ViewTranslateAnimation create() {
        setFillEnabled(true);
        setFillAfter(true);
        return this;
    }

    /**
     * 开始执行动画
     */
    public void start() {
        for (View view : views) {
            view.startAnimation(this);
        }
    }
}