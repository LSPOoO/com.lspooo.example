package com.lspooo.plugin.statistics.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lspooo.plugin.common.common.BroadcastIntent;
import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.TabFragment;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.DBTeaEmployeeTools;
import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;
import com.lspooo.plugin.statistics.ui.adapter.TeaEmployeeListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LSP on 2017/10/10.
 */

public class TabTeaEmployeeFragment extends TabFragment{

    private RecyclerView mRecyclerView;
    private TeaEmployeeListAdapter mAdapter;
    private List<TeaEmployee> employeeList = new ArrayList<>();

    public static TabTeaEmployeeFragment newInstance() {
        TabTeaEmployeeFragment fragment = new TabTeaEmployeeFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadEmployee();
        registerReceiver(new String[]{BroadcastIntent.ACTION_SYNC_TEA_EMPLOYEE});
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new TeaEmployeeListAdapter(R.layout.layout_tea_employee_item, employeeList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TeaEmployee employee = employeeList.get(position);
                if (employee == null){
                    return;
                }
                Intent intent = new Intent(getActivity(), TeaEmployeeDetailActivity.class);
                intent.putExtra("id", employee.getId());
                startActivity(intent);
            }
        });
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                return true;
            }
        });
    }

    private void loadEmployee() {
        List<TeaEmployee> result = DBTeaEmployeeTools.getInstance().query();
        employeeList.clear();
        employeeList.addAll(result);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void handleReceiver(Context context, Intent intent) {
        super.handleReceiver(context, intent);
        if (BroadcastIntent.ACTION_SYNC_TEA_EMPLOYEE.equals(intent.getAction())){
            loadEmployee();
        }
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
