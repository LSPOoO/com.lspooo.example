package com.lspooo.example.ui.tea;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.lspooo.example.R;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.BaseFragment;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.statistics.ui.TabTeaEmployeeFragment;

/**
 * Created by LSP on 2017/10/11.
 */

public class TeaLauncherUI extends CommonActivity{

    private TeaPlusSubMenuHelper mTeaPlusSubMenuHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        hideHomeActionMenu();
        setActionBarTitle(getString(R.string.app_name));
        setActionMenuItem(0, R.drawable.ic_more_white, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                controlPlusSubMenu();
                return true;
            }
        });
        BaseFragment newFragment = TabTeaEmployeeFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout_container, newFragment);
        transaction.commit();
    }

    private void controlPlusSubMenu() {
        if (mTeaPlusSubMenuHelper == null) {
            mTeaPlusSubMenuHelper = new TeaPlusSubMenuHelper(this);
        }
        if (mTeaPlusSubMenuHelper.isShowing()) {
            mTeaPlusSubMenuHelper.dismiss();
            return;
        }
        mTeaPlusSubMenuHelper.setOnDismissListener(null);
        mTeaPlusSubMenuHelper.tryShow();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mTeaPlusSubMenuHelper != null && mTeaPlusSubMenuHelper.isShowing()) {
            mTeaPlusSubMenuHelper.dismiss();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                && event.getAction() == KeyEvent.ACTION_UP) {
            if (mTeaPlusSubMenuHelper != null && mTeaPlusSubMenuHelper.isShowing()) {
                mTeaPlusSubMenuHelper.dismiss();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tea_launcher;
    }


    @Override
    protected boolean isEnableSwipe() {
        return false;
    }
}
