/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.lspooo.plugin.common.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 重载接口增加设置主界面是否可以进行滑动
 * Created by Jorstin on 2015/3/18.
 */
public class CustomViewPager extends ViewPager {

    /**
     * 控制页面是否可以左右滑动
     */
    private boolean mSlidenabled = true;

    /**
     * @param context
     */
    public CustomViewPager(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置是否可以滑动
     */
    public final void setSlideEnabled(boolean enabled) {
        mSlidenabled = enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (!mSlidenabled) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mSlidenabled) {
            return false;
        }
        try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSlidenabled;
    }

}

