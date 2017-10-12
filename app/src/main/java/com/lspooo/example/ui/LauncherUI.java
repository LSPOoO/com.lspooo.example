package com.lspooo.example.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.lspooo.example.R;
import com.lspooo.example.view.LauncherBottomTabLayout;
import com.lspooo.example.view.LauncherBottomTabView;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.common.view.CustomViewPager;

/**
 * Created by LSP on 2017/9/21.
 */

public class LauncherUI extends CommonActivity {

    private CustomViewPager mViewPager;
    private LauncherViewPagerAdapter mAdapter;
    private LauncherBottomTabLayout launcherBottomTabLayout;
    private PlusSubMenuHelper mPlusSubMenuHelper;
    private int currentTabIndex = LauncherBottomTabView.TAB_COMMUNICATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideHomeActionMenu();
        setActionBarTitle(getString(R.string.app_name));
        setActionMenuItem(0, R.drawable.ic_more_white, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                controlPlusSubMenu();
                return true;
            }
        });
        initLauncherViewPager();
    }

    private void initLauncherViewPager(){
        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        if (mViewPager != null) {
            mViewPager.setOffscreenPageLimit(3);
            mViewPager.setSlideEnabled(false);
        }
        mAdapter = new LauncherViewPagerAdapter(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        launcherBottomTabLayout = (LauncherBottomTabLayout) findViewById(R.id.launcherBottomTabView);
        launcherBottomTabLayout.setTabSelectedListener(tabSelectedListener);
        launcherBottomTabLayout.setTabSelected(LauncherBottomTabView.TAB_COMMUNICATION);
    }

    private void controlPlusSubMenu() {
        if (mPlusSubMenuHelper == null) {
            mPlusSubMenuHelper = new PlusSubMenuHelper(this);
        }
        if (mPlusSubMenuHelper.isShowing()) {
            mPlusSubMenuHelper.dismiss();
            return;
        }
        mPlusSubMenuHelper.setOnDismissListener(null);
        mPlusSubMenuHelper.tryShow();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mPlusSubMenuHelper != null && mPlusSubMenuHelper.isShowing()) {
            mPlusSubMenuHelper.dismiss();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                && event.getAction() == KeyEvent.ACTION_UP) {
            if (mPlusSubMenuHelper != null && mPlusSubMenuHelper.isShowing()) {
                mPlusSubMenuHelper.dismiss();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private LauncherBottomTabLayout.OnTabSelectedListener tabSelectedListener = new LauncherBottomTabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(int tabIndex) {
            if (currentTabIndex == tabIndex){
                return;
            }
            currentTabIndex = tabIndex;
            mViewPager.setCurrentItem(tabIndex, false);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
