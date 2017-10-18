/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.lspooo.plugin.common.tools;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.SparseIntArray;

import com.lspooo.plugin.common.LSPApplicationContext;


public class ResourceHelper {

    private static final String TAG = "RongXin.ResourceHelper";

    private static final String TEXT_SIZE_SCALE_KEY = "text_size_scale_key";
    private static float density = -1.0F;
    private static IResource mResource = null;
    private static SparseIntArray mPixelSize = new SparseIntArray();
    private static float mDefaultScale = 0.0F;

    /**
     * 根据提供的颜色资源返回颜色状态集合
     * @param context 上下文
     * @param resId 颜色资源
     * @return 颜色状态集合
     */
    public static ColorStateList getColorStateList(Context context, int resId) {
        if(mResource != null) {
            return mResource.getColorStateList();
        }
        if(context == null) {
            return null;
        }
        return context.getResources().getColorStateList(resId);
    }

    /**
     * 根据提供的尺寸资源进行转换
     * @param context 上下文
     * @param resId 资源文件
     * @return 转换结果
     */
    public static int getDimensionPixelSize(Context context, int resId) {
        int result = 0;
        if(mResource != null) {
            result = mPixelSize.get(resId, 0);
            if(result == 0) {
                result = mResource.getDimensionPixelSize();
                mPixelSize.put(resId, result);
            }
            return (int)((float)result * mDefaultScale);
        }

        if(context == null) {
            return result;
        }

        result = mPixelSize.get(resId, 0);
        if(result == 0) {
            result = context.getResources().getDimensionPixelSize(resId);
            mPixelSize.put(resId, result);
        }
        return (int)((float)result * mDefaultScale);

    }

    /**
     * 返回字符串资源对应的文字
     * @param context 上下文
     * @param resId 字符串资源
     * @return 字符串资源文字
     */
    public static String getString(Context context, int resId) {
        if(mResource == null) {
            return mResource.getString();
        }

        if(context == null) {
            return "";
        }
        return context.getResources().getString(resId);
    }

    /**
     * 取整
     * @param context 上下文
     * @param value 参数
     * @return 结果
     */
    public static int round(Context context, int value) {
        return Math.round((float)value / getDensity(context));
    }

    /**
     * 根据提高的资源id返回一个drawable对象
     * @param context 上下文
     * @param resId 资源id
     * @return Drawable
     */
    public static Drawable getDrawable(Context context, int resId) {
        if(mResource != null) {
            return mResource.getDrawable();
        }
        if(context == null) {
            return null;
        }

        return context.getResources().getDrawable(resId);
    }

    public static Bitmap getBitmap(Context context ,int resId) {
        if(context == null) {
            context = LSPApplicationContext.getContext();
        }
        return BitmapFactory.decodeResource(context.getResources(), resId);

    }

    /**
     * 返回一个颜色值
     * @param context 上下文
     * @param resId 颜色资源文件
     * @return 颜色值
     */
    public static int getColor(Context context, int resId) {
        if(mResource != null) {
            return mResource.getColor();
        }
        if(context == null) {
            return 0;
        }
        return context.getResources().getColor(resId);
    }


    /**
     * 获取屏幕宽度（分辨率）
     * @param context 上下文
     * @return 屏幕宽度（分辨率）
     */
    public static int getWidthPixels(Context context) {
        int widthPixels = 0;
        if(mResource == null) {
            if(context == null) {
            } else {
                widthPixels =  context.getResources().getDisplayMetrics().widthPixels;
            }
        }

        return widthPixels;
    }

    /**
     * 获取屏幕高度（分辨率）
     * @param context 上下文
     * @return 屏幕高度（分辨率）
     */
    public static int getHeightPixels(Context context) {
        int heightPixels = 0;
        if(mResource == null) {
            if(context == null) {
            } else {
                heightPixels = context.getResources().getDisplayMetrics().heightPixels;
            }
        }
        return heightPixels;
    }

    /**
     * 密度转换
     * @param context 上下文
     * @param value 需要转换的值
     * @return 转换结果
     */
    public static int fromDPToPix(Context context, int value) {
        return Math.round(getDensity(context) * (float)value);
    }

    /**
     * 返回当前手机屏幕分辨率密度
     * @param context 上下文
     * @return 屏幕分辨率密度
     */
    public static float getDensity(Context context) {
        if(context == null) {
            context = LSPApplicationContext.getContext();
        }

        if(density < 0.0F) {
            density = context.getResources().getDisplayMetrics().density;
        }

        return density;
    }

    /**
     * 资源加载接口
     */
    public interface IResource {

        /**
         * 返回一个颜色值
         * @return 颜色值
         */
        int getColor();

        /**
         * 根据提供的颜色资源返回颜色状态集合
         * @return 颜色状态集合
         */
        ColorStateList getColorStateList();

        /**
         * 根据提高的资源id返回一个drawable对象
         * @return Drawable
         */
        Drawable getDrawable();

        /**
         * 根据提供的尺寸资源进行转换
         * @return 转换结果
         */
        int getDimensionPixelSize();

        /**
         * 返回字符串资源对应的文字
         * @return 字符串资源文字
         */
        String getString();
    }
}
