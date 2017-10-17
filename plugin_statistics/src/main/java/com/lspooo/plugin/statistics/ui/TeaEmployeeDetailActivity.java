package com.lspooo.plugin.statistics.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lspooo.plugin.common.common.BroadcastIntent;
import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.common.menu.ActionMenu;
import com.lspooo.plugin.common.common.menu.OverflowSubMenuHelper;
import com.lspooo.plugin.common.common.menu.RXContentMenuHelper;
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
    private RXContentMenuHelper mMenuHelper;

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
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                TeaRecord record = recordList.get(position);
                if (position >= recordList.size()){
                    return true;
                }
                if (record == null){
                    return true;
                }
                showMenu(record);
                return true;
            }
        });
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

    private void showMenu(final TeaRecord record){
        if (mMenuHelper == null){
            mMenuHelper = new RXContentMenuHelper(this);
        }
        mMenuHelper.setOnCreateActionMenuListener(new ActionMenu.OnCreateActionMenuListener() {
            @Override
            public void OnCreateActionMenu(ActionMenu menu) {
                menu.add(1 , R.string.app_del);
            }
        });
        mMenuHelper.setOnActionMenuItemSelectedListener(new ActionMenu.OnActionMenuItemSelectedListener() {
            @Override
            public void OnActionMenuSelected(MenuItem menu, int position) {
                switch (menu.getItemId()){
                    case 1:
                        recordList.remove(record);
                        mAdapter.notifyDataSetChanged();
                        DBTeaRecordTools.getInstance().delete(record);
                        break;
                    default:
                        break;
                }
            }
        });
        mMenuHelper.show();
    }

    @Override
    protected String[] getReceiverAction() {
        return new String[]{BroadcastIntent.ACTION_SYNC_TEA_RECORD};
    }

    @Override
    protected void onHandleReceiver(Context context, Intent intent) {
        super.onHandleReceiver(context, intent);
        if (BroadcastIntent.ACTION_SYNC_TEA_RECORD.equals(intent.getAction())){
            loadTeaRecord();
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
