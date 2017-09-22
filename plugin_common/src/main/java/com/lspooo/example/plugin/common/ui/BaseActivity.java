package com.lspooo.example.plugin.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lspooo.example.plugin.common.R;
import com.lspooo.example.plugin.common.common.menu.ActionMenuItem;
import com.lspooo.example.plugin.common.tools.SystemBarTintManager;

/**
 * Created by LSP on 2017/9/21.
 */

public abstract class BaseActivity extends AbsActivity{

    private LayoutInflater mLayoutInflater;
    private SystemBarTintManager mSystemBarTintManager;

    protected View mBaseLayoutView;
    private View mTransLayerView;
    private FrameLayout mContentFrameLayout;
    protected View mContentView;

    private ActionBar mActionBar;
    private Toolbar mToolBar;
    private View mActionBarLayout;
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mTitleSummaryView;
    private View mActionBarShadow;

    private ActionMenuItem mDisplayHomeMenu = new ActionMenuItem();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!allowPlenaryContentView()){
            return;
        }
        init();
    }

    private void init() {
        mSystemBarTintManager = new SystemBarTintManager(this);
        int layoutId = getLayoutId();
        mLayoutInflater = LayoutInflater.from(this);
        mBaseLayoutView = mLayoutInflater.inflate(R.layout.lsp_activity, null);

        mTransLayerView = mBaseLayoutView.findViewById(R.id.ytx_trans_layer);
        mContentFrameLayout = (FrameLayout) mBaseLayoutView.findViewById(R.id.ytx_content_fl);
        ViewGroup mRootView = (ViewGroup) mBaseLayoutView.findViewById(R.id.ytx_root_view);

        if (hasActionBar()){
            mActionBarLayout = mLayoutInflater.inflate(R.layout.recycler_view_toolbar, null);
            Toolbar toolbar = (Toolbar) mActionBarLayout.findViewById(R.id.toolbar);
            mTitleView = (TextView) mActionBarLayout.findViewById(R.id.action_title);
            mSubTitleView = (TextView) mActionBarLayout.findViewById(R.id.action_sub_title);
            mTitleSummaryView = (TextView) mActionBarLayout.findViewById(R.id.action_title_count);
            mActionBarShadow = mActionBarLayout.findViewById(R.id.toolbar_shadow);
            mRootView.addView(mActionBarLayout,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                mToolBar = toolbar;
                if (buildActionBarPadding()) {
                    // 设置状态栏和顶部有一个padding间距
                    mRootView.setPadding(mRootView.getPaddingLeft(), mSystemBarTintManager.getConfig().getStatusBarHeight(), mRootView.getPaddingRight(), mRootView.getPaddingBottom());
                }
                mActionBar = getSupportActionBar();
               /* if (mActionBar != null) {
                    mActionBar.setHomeAsUpIndicator(mActionBarActivity.getResources().getDrawable(R.drawable.ic_arrow_back_24));
                }*/
            }
        }
        if (layoutId != -1) {
            mContentView = mLayoutInflater.inflate(layoutId, null);
            mRootView.addView(mContentView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }
        setContentView(mBaseLayoutView);
        initSwipeLayout();
        setNavigationOnClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onDisplayHomeAsUp();
                return false;
            }
        });
    }

    /**
     * 设置标题栏返回菜单事件监听
     * @param listener 事件监听
     */
    void setNavigationOnClickListener(MenuItem.OnMenuItemClickListener listener) {
        if (mActionBar == null) {
            return;
        }
        if (listener == null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDisplayHomeMenu.setMenuId(android.R.id.home);
        mDisplayHomeMenu.setItemClickListener(listener);
    }

    /**
     * 默认关闭当前页面
     */
    public void onDisplayHomeAsUp() {
        hideSoftKeyboard();
        finish();
    }

    /**
     * 是否需要全权交由框架处理初始化当前UI主界面
     * 1、会增加初始化状态栏、标题栏
     * 2、会提供默认的设置状态栏的接口等
     *
     * @return 是否需要初始化
     */
    public boolean allowPlenaryContentView() {
        return true;
    }


    public abstract int getLayoutId();
    public abstract boolean hasActionBar();
    public abstract boolean buildActionBarPadding();
}
