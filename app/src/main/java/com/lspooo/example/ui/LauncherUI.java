package com.lspooo.example.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lspooo.example.R;
import com.lspooo.example.view.DrawerMenuView;
import com.lspooo.example.view.LauncherBottomTabLayout;
import com.lspooo.example.view.LauncherBottomTabView;
import com.lspooo.plugin.common.tools.StatusBarUtilsForDrawerLayout;
import com.lspooo.plugin.common.view.CustomViewPager;

/**
 * Created by LSP on 2017/9/21.
 */

public class LauncherUI extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private CustomViewPager mViewPager;
    private LauncherViewPagerAdapter mAdapter;
    private LauncherBottomTabLayout launcherBottomTabLayout;
    private int currentTabIndex = LauncherBottomTabView.TAB_COMMUNICATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initToolbar();
        initLauncherViewPager();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        DrawerMenuView drawerMenuView = (DrawerMenuView) findViewById(R.id.drawer_menu_view);
        drawerMenuView.setCloseDrawerListener(new DrawerMenuView.OnCloseDrawerListener() {
            @Override
            public void onCloseDrawer() {
                mDrawerLayout.closeDrawers();
            }
        });
        StatusBarUtilsForDrawerLayout.setColorForDrawerLayout(this, mDrawerLayout, getResources().getColor(R.color.base_color));
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share_example) {
            Intent intent = new Intent(this, ShareExampleActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

}
