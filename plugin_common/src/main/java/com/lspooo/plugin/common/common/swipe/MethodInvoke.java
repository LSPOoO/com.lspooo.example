package com.lspooo.plugin.common.common.swipe;

import android.app.Activity;
import android.content.Context;

import com.lspooo.plugin.common.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 容联•云通讯
 * @since 2016-06-23
 */
public class MethodInvoke {
    private static final String TAG = "RongXin.MethodInvoke";

    public static int getTransitionValue(String componentName) {
        Class<?> clazz = getTransitionClass(componentName);
        if (clazz != null) {
            return getAnnotationValue(clazz);
        }
        return 0;
    }

    private static Class<?> getTransitionClass(String componentName) {
        try {
            return (Class) Class.forName(componentName);
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    public static int getAnnotationValue(Class<?> clazz) {
        ActivityTransition clazzAnnotation = clazz.getAnnotation(ActivityTransition.class);
        if (clazzAnnotation != null) {
            return clazzAnnotation.value();
        }
        while (clazz.getSuperclass() != null) {
            clazz = clazz.getSuperclass();
            clazzAnnotation = clazz.getAnnotation(ActivityTransition.class);
            if (clazzAnnotation != null) {
                return clazzAnnotation.value();
            }
        }
        return 0;
    }

    public static void startTransitionPopin(Context context) {
        if ((context == null) || (!(context instanceof Activity))) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.pop_in, R.anim.anim_not_change);
    }

    public static void startTransitionPopout(Context context) {
        if ((context == null) || (!(context instanceof Activity))) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.anim_not_change, R.anim.pop_out);
    }

    public static void startTransitionNotChange(Context context) {
        if ((context == null) || (!(context instanceof Activity))) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.anim_not_change, R.anim.anim_not_change);
    }


    public interface OnSwipeInvokeResultListener {
        void onSwipeInvoke(boolean success);
    }

    public static class SwipeInvocationHandler implements InvocationHandler {

        public WeakReference<OnSwipeInvokeResultListener> mWeakReference;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (mWeakReference == null) {
                return null;
            }
            OnSwipeInvokeResultListener onSwipeInvokeResultListener = mWeakReference.get();
            if (onSwipeInvokeResultListener == null) {
                return null;
            }
            boolean result = false;
            if (args != null) {
                if (args.length > 0) {
                    boolean isBoolArgs = (args[0] instanceof Boolean);
                    if (isBoolArgs) {
                        result = (Boolean) args[0];
                    }
                }
            }
            onSwipeInvokeResultListener.onSwipeInvoke(result);
            return null;
        }
    }

}
