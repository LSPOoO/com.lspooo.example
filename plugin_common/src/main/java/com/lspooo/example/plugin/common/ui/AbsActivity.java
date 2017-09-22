package com.lspooo.example.plugin.common.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.lspooo.example.plugin.common.R;
import com.lspooo.example.plugin.common.tools.AnimatorUtils;
import com.lspooo.example.plugin.common.tools.SwipTranslucentMethodUtils;
import com.lspooo.example.plugin.common.tools.SwipeActivityManager;
import com.lspooo.example.plugin.common.view.SwipeBackLayout;

/**
 * Created by LSP on 2017/9/21.
 */

public abstract class AbsActivity extends AppCompatActivity implements SwipeActivityManager.SwipeListener, SwipeBackLayout.OnSwipeGestureDelegate{

    public SwipeBackLayout mSwipeBackLayout;
    private InternalReceiver internalReceiver;
    public boolean mOnDragging;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!enableConvertFromTranslucent()) {
            SwipTranslucentMethodUtils.convertActivityFromTranslucent(this);
        }
        String[] actionArray = getReceiverAction();
        IntentFilter intentfilter = new IntentFilter();
        if (actionArray != null) {
            for (String action : actionArray) {
                intentfilter.addAction(action);
            }
        }
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        registerReceiver(internalReceiver, intentfilter);
    }

    protected boolean initSwipeLayout(){
        if (isSupperSwipe()) {
            ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
            mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                    R.layout.swipe_back_layout, viewGroup, false);
            mSwipeBackLayout.init();
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().getDecorView().setBackgroundDrawable(null);
            ViewGroup childAtView = (ViewGroup) viewGroup.getChildAt(0);
            childAtView.setBackgroundResource(R.color.transparent);
            viewGroup.removeView(childAtView);
            mSwipeBackLayout.addView(childAtView);
            mSwipeBackLayout.setContentView(childAtView);
            viewGroup.addView(this.mSwipeBackLayout);
            mSwipeBackLayout.setSwipeGestureDelegate(this);
            return true;
        }
        if(enableConvertFromTranslucent()) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return false;
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        return false;
    }

    /**
     * 是否window背景透明
     * @return 是否背景头像
     */
    public boolean enableConvertFromTranslucent() {
        return false;
    }

    /**
     * 当前是否支持侧滑控件的使用
     * @return 是否支持
     */
    private boolean isSupperSwipe() {
        if (isEnableSwipe()) {
            return true;
        }
        return false;
    }

    /**
     * 广播事件派发
     *
     * @param context 上下文
     * @param intent  广播数据
     */
    private void dispatchBroadcast(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        onHandleReceiver(context, intent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isSupperSwipe() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && mSwipeBackLayout != null && mSwipeBackLayout.isSwipeBacking()) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setEnableGesture(false);
        }
        if (!isFinishing()) {
            SwipeActivityManager.pushCallback(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SwipeActivityManager.popCallback(this);
        onScrollParent(1.0F);
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setEnableGesture(true);
            mSwipeBackLayout.mScrolling = false;
        }
    }

    @Override
    public void onScrollParent(float scrollPercent) {
        View decorView = getWindow().getDecorView();
        if ((decorView instanceof ViewGroup) && (((ViewGroup) decorView).getChildCount() > 0)) {
            decorView = ((ViewGroup) decorView).getChildAt(0);
        }
        if (Float.compare(1.0F, scrollPercent) <= 0) {
            AnimatorUtils.startViewAnimation(decorView, 0.0F);
            return;
        }
        AnimatorUtils.startViewAnimation(decorView, -1.0F * decorView.getWidth() / 4 * (1.0F - scrollPercent));
    }

    @Override
    public void notifySettle(boolean open, int speed) {
        View decorView = getWindow().getDecorView();
        if ((decorView instanceof ViewGroup) && (((ViewGroup) decorView).getChildCount() > 0)) {
            decorView = ((ViewGroup) decorView).getChildAt(0);
        }
        long duration = 120L;
        if (speed <= 0) {
            duration = 240L;
        }
        if (open) {
            AnimatorUtils.updateViewAnimation(decorView, duration, 0.0F, null);
            return;
        }
        AnimatorUtils.updateViewAnimation(decorView, duration, -1 * decorView.getWidth() / 4, null);
    }

    @Override
    public boolean isEnableGesture() {
        return false;
    }

    @Override
    public void onSwipeBack() {
        if (!(isFinishing())) {
            hideSoftKeyboard();
            finish();
        }
        mOnDragging = false;
    }

    @Override
    public void onDragging() {
        mOnDragging = true;
    }

    @Override
    public void onCancel() {
        mOnDragging = false;
    }

    /**
     * 这里用来处理整个Window窗口的背景
     */
    @Override
    public void onDealDecorView() {
        // 默认窗口的背景颜色为透明色
    }

    /**
     * 隐藏键盘
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            unregisterReceiver(internalReceiver);
        } catch (Exception e) {
        }
    }

    /**
     * 需要注册的广播
     * @return 广播数组
     */
    protected String[] getReceiverAction() {
        return null;
    }

    /**
     * 如果子界面需要拦截处理注册的广播
     * 需要实现该方法
     * @param context 上下文
     * @param intent  意图
     */
    protected void onHandleReceiver(Context context, Intent intent) {

    }

    /**
     * 判断当前是否需要使用侧滑控件来处理
     * @return 是否需要侧滑
     */
    protected boolean isEnableSwipe() {
        return true;
    }

    /**
     * 广播内部处理分发
     */
    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            dispatchBroadcast(context, intent);
        }
    }
}
