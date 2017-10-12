package com.lspooo.example.ui.tea;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.lspooo.example.R;
import com.lspooo.example.ui.PlusSubMenuHelper;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.BaseFragment;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.statistics.ui.TabTeaEmployeeFragment;

/**
 * Created by LSP on 2017/10/11.
 */

public class TeaLauncherUI extends CommonActivity{

    private PlusSubMenuHelper mPlusSubMenuHelper;

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


    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tea_launcher;
    }
}
