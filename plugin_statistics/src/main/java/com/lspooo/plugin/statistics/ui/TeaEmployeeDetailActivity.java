package com.lspooo.plugin.statistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.lspooo.plugin.common.common.menu.ActionMenu;
import com.lspooo.plugin.common.common.menu.OverflowSubMenuHelper;
import com.lspooo.plugin.common.common.toast.ToastUtil;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.DBTeaEmployeeTools;
import com.lspooo.plugin.statistics.dao.bean.DBTeaRecordTools;
import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;
import com.lspooo.plugin.statistics.ui.adapter.TeaRecordListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LSP on 2017/10/11.
 */

public class TeaEmployeeDetailActivity extends CommonActivity{


    private RecyclerView mRecyclerView;
    private TeaRecordListAdapter mAdapter;

    private TeaEmployee employee;
    private OverflowSubMenuHelper mOverflowHelper;
    private List<TeaRecord> recordList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getIntent().getLongExtra("id", -1);
        if (id == -1){
            ToastUtil.error("该采茶工不存在！");
            finish();
            return;
        }
        employee = DBTeaEmployeeTools.getInstance().queryTeaEmployee(id);
        if (employee == null){
            ToastUtil.error("该采茶工不存在！");
            finish();
            return;
        }
        initView();
        loadTeaRecord();
    }

    private void initView() {
        setActionBarTitle(employee.getId() + ".  " + employee.getName());
        setActionMenuItem(0, R.drawable.ic_more_white, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                initOverflowSubMenuAction();
                return true;
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new TeaRecordListAdapter(R.layout.layout_tea_record_item, recordList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initOverflowSubMenuAction() {
        if(mOverflowHelper == null) {
            mOverflowHelper = new OverflowSubMenuHelper(TeaEmployeeDetailActivity.this);
            mOverflowHelper.setOnActionMenuItemSelectedListener(new ActionMenu.OnActionMenuItemSelectedListener() {
                @Override
                public void OnActionMenuSelected(MenuItem menu, int position) {
                    switch (menu.getItemId()){
                        case 0:
                            Intent intent = new Intent(TeaEmployeeDetailActivity.this, AddTeaRecordActivity.class);
                            intent.putExtra("employeeId", employee.getId());
                            startActivity(intent);
                            break;
                        case 1:
                            break;
                    }
                }
            });
            mOverflowHelper.setOnCreateActionMenuListener(new ActionMenu.OnCreateActionMenuListener() {
                @Override
                public void OnCreateActionMenu(ActionMenu menu) {
                    menu.add(0, R.string.add_tea_record , R.drawable.ic_close_black);
                    menu.add(1, R.string.del_tea_employee , R.drawable.ic_close_black);
                }
            });
        }
        mOverflowHelper.tryShow();
    }

    private void loadTeaRecord(){
        recordList.clear();
        List<TeaRecord> result = DBTeaRecordTools.getInstance().queryTeaRecord(employee.getId());
        if (result != null && result.size() > 0){
            recordList.addAll(result);
            mAdapter.notifyDataSetChanged();
        } else{
            mAdapter.setEmptyView(R.layout.layout_empty_tea_record, (ViewGroup) mRecyclerView.getParent());
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tea_employee_detail;
    }
}
