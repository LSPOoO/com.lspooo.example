package com.lspooo.plugin.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.TabFragment;

/**
 * Created by LSP on 2017/10/10.
 */

public class TabTeaEmployeeFragment extends TabFragment{

    private RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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
        return R.layout.fragment_tea_employee;
    }
}
