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
import android.widget.TextView;

import com.lspooo.plugin.common.common.BroadcastIntent;
import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.common.menu.ActionMenu;
import com.lspooo.plugin.common.common.menu.OverflowSubMenuHelper;
import com.lspooo.plugin.common.common.menu.RXContentMenuHelper;
import com.lspooo.plugin.common.common.toast.ToastUtil;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.tools.DateStyle;
import com.lspooo.plugin.common.tools.DateUtil;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.DBTeaEmployeeTools;
import com.lspooo.plugin.statistics.dao.bean.DBTeaRecordTools;
import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;
import com.lspooo.plugin.statistics.ui.adapter.TeaRecordListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LSP on 2017/10/11.
 */

public class TeaEmployeeDetailActivity extends CommonActivity{

    private RecyclerView mRecyclerView;
    private TeaRecordListAdapter mAdapter;
    private View statisticsLayout;
    private TextView filterBtn;
    private TextView startTimeTv;
    private TextView endTimeTv;
    private TextView totalCountTv;
    private TextView totalWeightTv;

    private TeaEmployee employee;
    private OverflowSubMenuHelper mOverflowHelper;
    private RXContentMenuHelper mMenuHelper;

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
        loadTeaRecord(0, 0);
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

        statisticsLayout = findViewById(R.id.statisticsLayout);
        filterBtn = (TextView) findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordList.size() <= 0){
                    return;
                }
                Intent intent = new Intent(TeaEmployeeDetailActivity.this, DateFilterSelectActivity.class);
                intent.putExtra("startDate", Long.parseLong(recordList.get(0).getTime()));
                intent.putExtra("endDate", Long.parseLong(recordList.get(recordList.size() - 1).getTime()));
                startActivityForResult(intent, 0x1);

//                ECAlertDialog dialog = ECAlertDialog.buildAlert(TeaEmployeeDetailActivity.this, "哈哈哈哈", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                dialog.setTitle("提示");
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
//                dialog.setCancelable(false);
//                dialog.show();
            }
        });
        startTimeTv = (TextView) findViewById(R.id.startTimeTv);
        endTimeTv = (TextView) findViewById(R.id.endTimeTv);
        totalCountTv = (TextView) findViewById(R.id.totalCountTv);
        totalWeightTv = (TextView) findViewById(R.id.totalWeightTv);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new TeaRecordListAdapter(R.layout.layout_tea_record_item, recordList, false);
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

    private void loadTeaRecord(long start, long end){
        recordList.clear();
        List<TeaRecord> result;
        if (start > 0 && end > 0){
            result = DBTeaRecordTools.getInstance().queryTeaRecord(employee.getId(), start + "", end + "");
        } else{
            result = DBTeaRecordTools.getInstance().queryTeaRecord(employee.getId());
        }
        if (result != null && result.size() > 0){
            recordList.addAll(result);
            mAdapter.notifyDataSetChanged();
            statisticsLayout.setVisibility(View.VISIBLE);
            startTimeTv.setText(DateUtil.DateToString(new Date(start > 0 ? start : Long.parseLong(recordList.get(0).getTime())), DateStyle.YYYY_MM_DD));
            endTimeTv.setText(DateUtil.DateToString(new Date(end > 0 ? end : Long.parseLong(recordList.get(recordList.size() - 1).getTime())), DateStyle.YYYY_MM_DD));
            totalCountTv.setText("采茶次数：" + recordList.size() + "次");
            float sum = 0;
            for (TeaRecord record : recordList){
                sum += record.getWeight();
            }
            totalWeightTv.setText("采茶重量总计：" + sum + "Kg");
        } else{
            mAdapter.setEmptyView(R.layout.layout_empty_tea_record, (ViewGroup) mRecyclerView.getParent());
            statisticsLayout.setVisibility(View.GONE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1){
            if (resultCode != RESULT_OK || data == null){
                return;
            }
            long startDate = data.getLongExtra("startDate", -1);
            long endDate = data.getLongExtra("endDate", -1);
            loadTeaRecord(startDate, endDate);
        }
    }

    @Override
    protected String[] getReceiverAction() {
        return new String[]{BroadcastIntent.ACTION_SYNC_TEA_RECORD};
    }

    @Override
    protected void onHandleReceiver(Context context, Intent intent) {
        super.onHandleReceiver(context, intent);
        if (BroadcastIntent.ACTION_SYNC_TEA_RECORD.equals(intent.getAction())){
            loadTeaRecord(0, 0);
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
