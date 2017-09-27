package com.lspooo.example.setting.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lspooo.example.R;
import com.lspooo.example.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.example.plugin.common.ui.CommonActivity;

/**
 * Created by LSP on 2017/9/25.
 */

public class SettingAccountInfoActivity extends CommonActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_account;
    }
}
