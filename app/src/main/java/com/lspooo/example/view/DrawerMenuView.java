package com.lspooo.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.lspooo.example.R;

/**
 * Created by lspooo on 2018/5/9.
 */

public class DrawerMenuView extends LinearLayout{

    private Context context;
    private OnCloseDrawerListener closeDrawerListener;

    public DrawerMenuView(Context context) {
        this(context, null);
    }

    public DrawerMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.layout_navigation_header, this, true);
    }

    public void setCloseDrawerListener(OnCloseDrawerListener closeDrawerListener) {
        this.closeDrawerListener = closeDrawerListener;
    }

    public interface OnCloseDrawerListener{
        void onCloseDrawer();
    }
}
