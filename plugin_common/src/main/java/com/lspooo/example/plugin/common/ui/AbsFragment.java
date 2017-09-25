package com.lspooo.example.plugin.common.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.lspooo.example.plugin.common.LSPApplicationContext;

public abstract class AbsFragment extends Fragment {

    protected boolean isDestroy = false;
    private FragmentActivity mActivity;
    protected boolean isFinish = false;

    public AbsFragment() {
        isFinish = false;
    }

    /**
     * 标记是否关闭
     * @param finish 是否关闭
     */
    public AbsFragment(boolean finish) {
        isFinish = finish;
    }

    /**
     * 标记是否可用关闭
     * @param isFinish 是否可用关闭
     */
    public void markFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    /**
     * 打开偏好设置属性文件
     * @param name 文件名
     * @param mode 打开模式
     * @return 访问接口
     */
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return getFragmentActivity().getSharedPreferences(name, mode);
    }

    /**
     * 从Intent中读取long属性值如果存在的情况下，否则返回-1;
     * @param name 属性key
     * @return 属性值
     */
    public long getValue(String name) {
        if(isFinish && super.getArguments() == null) {
            return getFragmentActivity().getIntent().getLongExtra(name , -1L);
        } else if (getArguments() != null) {
            return super.getArguments().getLong(name , -1L);
        }
        return -1L;
    }

    /**
     * 从Intent中读取boolean属性值如果存在的情况下
     * @param key 属性key
     * @param defaultValue 属性默认值
     * @return 属性值
     */
    public Boolean getBooleanValue(String key, boolean defaultValue) {
        Boolean def ;
        if(this.isFinish && super.getArguments() == null) {
            def = getFragmentActivity().getIntent().getBooleanExtra(key, defaultValue);
        } else {
            def = super.getArguments().getBoolean(key, defaultValue);
        }

        return def;
    }

    /**
     * 设置取消模式
     */
    public void setResultCancel() {
        if(isFinish) {
            getFragmentActivity().setResult(Activity.RESULT_CANCELED);
        }
    }

    /**
     * 根据给定的资源ID 查找资源
     * @param id 资源ID
     * @return 资源View
     */
    public final View findViewById(int id) {
        View view = super.getView();
        View returnView = null;
        if(view != null) {
            returnView = view.findViewById(id);
        }

        if(returnView == null) {
            returnView = getFragmentActivity().findViewById(id);
        }
        return returnView;
    }

    /**
     * 从Intent中读取int属性值如果存在的情况下
     * @param key 属性key
     * @param defaultValue 属性默认值
     * @return 属性值
     */
    public int getIntExtra(String key, int defaultValue) {
        int intValue;
        if(this.isFinish && super.getArguments() == null && this.getFragmentActivity() != null) {
            intValue = this.getFragmentActivity().getIntent().getIntExtra(key, defaultValue);
        } else {
            intValue = defaultValue;
            if(super.getArguments() != null) {
                intValue = super.getArguments().getInt(key, defaultValue);
            }
        }

        return intValue;
    }

    /**
     * 从Intent中读取String属性值如果存在的情况下
     * @param key 属性key
     * @return 属性值
     */
    public String getStringExtra(String key) {
        String strValue = null;
        if(this.isFinish) {
            if(this.getFragmentActivity() != null) {
                strValue = this.getFragmentActivity().getIntent().getStringExtra(key);
            }
        }
        if(strValue == null) {
            if(super.getArguments() != null) {
                strValue = super.getArguments().getString(key);
            }
        }
        return strValue;
    }

    /**
     * 获取一个窗口管理器
     * @return 窗口管理器
     */
    public final WindowManager getWindowManager() {
        return getFragmentActivity() != null ? getFragmentActivity().getWindowManager() : null;
    }

    /**
     * 是否关闭
     * @return 是否关闭
     */
    public boolean isFinishing() {
        return getFragmentActivity() == null || getFragmentActivity().isFinishing();
    }

    /**
     * 当前是否正在显示
     * @return 是否正在显示
     */
    public final boolean isShowing() {
        return !isDestroy;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }


    /**
     * 当前Fragment承载的Activity
     * @return Fragment承载的Activity
     */
    public FragmentActivity getFragmentActivity() {
        if(mActivity == null) {
            mActivity = super.getActivity();
        }
        return mActivity;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    /**
     * 处理按下事件
     * @param keyCode 状态码
     * @param event 事件
     * @return 是否消费
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK) && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
            return onKeyUp(keyCode, event);
        }
        return false;
    }

    /**
     * 处理抬起事件
     * @param keyCode 状态码
     * @param event 事件
     * @return 是否消费
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 打开菜单
     * @param view 菜单所在的View
     */
    public void openContextMenu(View view) {
        if(getFragmentActivity() != null) {
            getFragmentActivity().openContextMenu(view);
        }
    }

    /**
     * 执行Activity切换动画
     * @param enterAnim 进入动画
     * @param exitAnim 退出动画
     */
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        if(getFragmentActivity() != null) {
            getFragmentActivity().overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * 发送广播
     * @param intent 参数
     */
    public void sendBroadcast(Intent intent) {
        if(getFragmentActivity() != null) {
            getFragmentActivity().sendBroadcast(intent);
        }
    }

    /**
     * 设置当前的屏幕方向
     * @param requestedOrientation 屏幕方向
     */
    public void setRequestedOrientation(int requestedOrientation) {
        if(getFragmentActivity() != null) {
            getFragmentActivity().setRequestedOrientation(requestedOrientation);
        }
    }

    /**
     * 打开Activity
     * @param intent 参数
     */
    public void startActivity(Intent intent) {
        FragmentActivity activity = getFragmentActivity();
        if(activity == null) {
            LSPApplicationContext.getContext().startActivity(intent);
        } else {
            activity.startActivityFromFragment(this , intent , -1);
        }
    }

    @SuppressLint("NewApi")
    public void finish() {
        if (isFinish) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        } else if(getFragmentActivity() != null) {
            getFragmentActivity().getFragmentManager().popBackStack();
        }
    }
}
