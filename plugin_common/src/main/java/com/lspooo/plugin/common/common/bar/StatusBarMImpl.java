package com.lspooo.plugin.common.common.bar;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 兼容M版本
 *
 * @author 容联•云通讯
 * @since 2017-05-12
 */
class StatusBarMImpl implements IStatusBar {
    @TargetApi(Build.VERSION_CODES.M)
    public void setStatusBarColor(Window window, int color) {
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(color);

        // View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // 不能设置以上这个属性，否者聊天界面键盘输入框会出现抖动使用 KPSwitchConflictUtil工具的时候
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        // 去掉系统状态栏下的windowContentOverlay
        View v = window.findViewById(android.R.id.content);
        if (v != null) {
            v.setForeground(null);
        }
    }

}
