package com.lspooo.plugin.common.common.swipe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;

import com.lspooo.plugin.common.tools.SDKVersionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SwipeTranslucentMethodUtils {

    private SwipeTranslucentMethodUtils() {
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static boolean convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityFromTranslucent(Activity)} .
     * <p>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void convertActivityToTranslucent(Activity activity , MethodInvoke.SwipeInvocationHandler handler) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }

            if(translucentConversionListenerClazz != null) {
                Object proxy = Proxy.newProxyInstance(translucentConversionListenerClazz.getClassLoader(), new Class[] {translucentConversionListenerClazz}, handler);
                if(!SDKVersionUtils.isGreatThanOrEqualTo(21)) {
                    Method method = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz);
                    method.setAccessible(true);
                    method.invoke(activity, proxy);
                } else {
                    Method method = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions.class);
                    method.setAccessible(true);
                    method.invoke(activity, proxy,null);
                }
            }
        } catch (Throwable t) {
        }
    }

}
