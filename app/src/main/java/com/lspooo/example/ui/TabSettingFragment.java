package com.lspooo.example.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lspooo.example.R;
import com.lspooo.example.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.example.plugin.common.ui.TabFragment;

/**
 * Created by LSP on 2017/9/24.
 */

public class TabSettingFragment extends TabFragment{

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        View viewById = findViewById(R.id.tab_setting);

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onTabFragmentClick() {

    }

    @Override
    public void onReleaseTabUI() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_setting;
    }
}
