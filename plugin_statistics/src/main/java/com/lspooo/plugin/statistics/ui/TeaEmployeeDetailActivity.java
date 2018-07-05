package com.lspooo.plugin.statistics.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lspooo.plugin.common.common.BroadcastIntent;
import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.common.dialog.ECAlertDialog;
import com.lspooo.plugin.common.common.menu.ActionMenu;
import com.lspooo.plugin.common.common.menu.OverflowSubMenuHelper;
import com.lspooo.plugin.common.common.menu.RXContentMenuHelper;
import com.lspooo.plugin.common.common.toast.ToastUtil;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.tools.DateStyle;
import com.lspooo.plugin.common.tools.DateUtil;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.R2;
import com.lspooo.plugin.statistics.common.Constant;
import com.lspooo.plugin.statistics.dao.bean.DBTeaEmployeeTools;
import com.lspooo.plugin.statistics.dao.bean.DBTeaRecordTools;
import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;
import com.lspooo.plugin.statistics.ui.adapter.TeaRecordListAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LSP on 2017/10/11.
 */

public class TeaEmployeeDetailActivity extends CommonActivity{

    private RecyclerView mRecyclerView;
    private TeaRecordListAdapter mAdapter;
    private View statisticsLayout;
    private View filterLayout;
    private TextView filterBtn;
    private TextView cancelBtn;
    private View timeLayout;
    private TextView nameTv;
    private TextView startTimeTv;
    private TextView endTimeTv;
    private TextView totalCountTv;
    private TextView totalWeightTv;

    private TeaEmployee employee;
    private OverflowSubMenuHelper mOverflowHelper;
    private RXContentMenuHelper mMenuHelper;

    private List<TeaRecord> recordList = new ArrayList<>();
    private long filterStart = 0;
    private long filterEnd = 0;

    private boolean isFilter = false;

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
        setActionBarTitle("采茶数据统计");
        setActionMenuItem(0, R.drawable.ic_more_white, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                initOverflowSubMenuAction();
                return true;
            }
        });

        statisticsLayout = findViewById(R.id.statisticsLayout);
        filterLayout = findViewById(R.id.filterLayout);
        filterBtn = (TextView) findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordList.size() <= 0){
                    return;
                }
                Intent intent = new Intent(TeaEmployeeDetailActivity.this, DateFilterSelectActivity.class);
                startActivityForResult(intent, 0x1);
            }
        });
        cancelBtn = (TextView) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterStart = 0;
                filterEnd = 0;
                isFilter = false;
                loadTeaRecord();
            }
        });
        timeLayout = findViewById(R.id.timeLayout);
        nameTv = (TextView) findViewById(R.id.nameTv);
        startTimeTv = (TextView) findViewById(R.id.startTimeTv);
        endTimeTv = (TextView) findViewById(R.id.endTimeTv);
        totalCountTv = (TextView) findViewById(R.id.totalCountTv);
        totalWeightTv = (TextView) findViewById(R.id.totalWeightTv);
        findViewById(R.id.addRecordBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeaEmployeeDetailActivity.this, InputTeaRecordActivity.class);
                intent.putExtra("employeeId", employee.getId());
                startActivity(intent);
            }
        });

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
        nameTv.setText(employee.getId() + ". " + employee.getName());
    }

    private void initOverflowSubMenuAction() {
        if(mOverflowHelper == null) {
            mOverflowHelper = new OverflowSubMenuHelper(TeaEmployeeDetailActivity.this);
            mOverflowHelper.setOnActionMenuItemSelectedListener(new ActionMenu.OnActionMenuItemSelectedListener() {
                @Override
                public void OnActionMenuSelected(MenuItem menu, int position) {
                    switch (menu.getItemId()){
                        case 0:
                            Intent intent = new Intent(TeaEmployeeDetailActivity.this, InputTeaRecordActivity.class);
                            intent.putExtra(Constant.EXTRA_TEA_RECORD_TYPE, Constant.TYPE_ADD);
                            intent.putExtra("employeeId", employee.getId());
                            startActivity(intent);
                            break;
                        case 1:
                            ECAlertDialog dialog = new ECAlertDialog.Builder(TeaEmployeeDetailActivity.this)
                                    .setTitle(R.string.app_dialog_title)
                                    .setMessage(R.string.confirm_delete_tea_employee)
                                    .setPositiveButton(R.string.app_dialog_confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).setPositiveColor(Color.RED)
                                    .setNegativeButton(R.string.app_dialog_cancel, null)
                                    .create();
                            if (dialog != null) {
                                dialog.show();
                            }
                            break;
                    }
                }
            });
            mOverflowHelper.setOnCreateActionMenuListener(new ActionMenu.OnCreateActionMenuListener() {
                @Override
                public void OnCreateActionMenu(ActionMenu menu) {
                    menu.add(0, R.string.add_tea_record);
                    menu.add(1, R.string.del_tea_employee);
                }
            });
        }
        mOverflowHelper.tryShow();
    }

    private void loadTeaRecord(){
        recordList.clear();
        List<TeaRecord> result;
        if (isFilter){
            result = DBTeaRecordTools.getInstance().queryTeaRecord(employee.getId(), filterStart + "", filterEnd + "");
        } else{
            result = DBTeaRecordTools.getInstance().queryTeaRecord(employee.getId());
        }
        if (result != null && result.size() > 0){
            recordList.addAll(result);
            mAdapter.notifyDataSetChanged();
        }
        initTeaStatisticLayout();
    }

    private void initTeaStatisticLayout(){

        if (recordList.size() > 0){
            totalCountTv.setText("采茶次数：" + recordList.size() + "次");
            Double sum = 0.0;
            for (TeaRecord record : recordList){
                BigDecimal a = new BigDecimal(String.valueOf(record.getWeight()));
                BigDecimal b = new BigDecimal(String.valueOf(sum));
                BigDecimal c = b.add(a);
                sum = c.doubleValue();
            }
            totalWeightTv.setText("采茶重量总计：" + sum + "斤");
            totalWeightTv.setVisibility(View.VISIBLE);
            filterLayout.setVisibility(View.VISIBLE);
        } else{
            totalWeightTv.setVisibility(View.GONE);
            if (isFilter){
                totalCountTv.setText("此段时间内该员工没有过采茶记录...");
                filterLayout.setVisibility(View.VISIBLE);
            } else{
                totalCountTv.setText("该员工尚未有过采茶记录...");
                filterLayout.setVisibility(View.GONE);
            }
        }

        if (isFilter){
            timeLayout.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
            startTimeTv.setText(DateUtil.DateToString(DateUtil.StringToDate(filterStart + "", DateStyle.YYYYMMDD), DateStyle.YYYY_MM_DD));
            endTimeTv.setText(DateUtil.DateToString(DateUtil.StringToDate(filterEnd + "", DateStyle.YYYYMMDD), DateStyle.YYYY_MM_DD));
        } else{
            timeLayout.setVisibility(View.INVISIBLE);
            cancelBtn.setVisibility(View.GONE);
        }
    }

    private void showMenu(final TeaRecord record){
        if (mMenuHelper == null){
            mMenuHelper = new RXContentMenuHelper(this);
        }
        mMenuHelper.setOnCreateActionMenuListener(new ActionMenu.OnCreateActionMenuListener() {
            @Override
            public void OnCreateActionMenu(ActionMenu menu) {
                menu.add(1 , R.string.app_modify);
                menu.add(2 , R.string.app_del);
            }
        });
        mMenuHelper.setOnActionMenuItemSelectedListener(new ActionMenu.OnActionMenuItemSelectedListener() {
            @Override
            public void OnActionMenuSelected(MenuItem menu, int position) {
                switch (menu.getItemId()){
                    case 1:
                        Intent intent = new Intent(TeaEmployeeDetailActivity.this, InputTeaRecordActivity.class);
                        intent.putExtra(Constant.EXTRA_TEA_RECORD_TYPE, Constant.TYPE_MODIFY);
                        intent.putExtra("record", record);
                        startActivity(intent);
                        break;
                    case 2:
                        ECAlertDialog dialog = new ECAlertDialog.Builder(TeaEmployeeDetailActivity.this)
                                .setTitle(R.string.app_dialog_title)
                                .setMessage(R.string.confirm_delete_tea_record)
                                .setPositiveButton(R.string.app_dialog_confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        recordList.remove(record);
                                        mAdapter.notifyDataSetChanged();
                                        initTeaStatisticLayout();
                                        DBTeaRecordTools.getInstance().delete(record);
                                    }
                                }).setPositiveColor(Color.RED)
                                .setNegativeButton(R.string.app_dialog_cancel, null)
                                .create();
                        if (dialog != null) {
                            dialog.show();
                        }
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
            filterStart = data.getLongExtra("startDate", -1);
            filterEnd = data.getLongExtra("endDate", -1);
            isFilter = true;
            loadTeaRecord();
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
