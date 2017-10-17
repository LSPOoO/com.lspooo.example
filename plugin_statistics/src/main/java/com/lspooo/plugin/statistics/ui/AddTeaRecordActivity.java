package com.lspooo.plugin.statistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lspooo.plugin.common.common.BroadcastIntent;
import com.lspooo.plugin.common.common.toast.ToastUtil;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.tools.DateStyle;
import com.lspooo.plugin.common.tools.DateUtil;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.common.view.TimeSelector;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.DBTeaRecordTools;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;

import java.util.Date;

/**
 * Created by LSP on 2017/10/12.
 */

public class AddTeaRecordActivity extends CommonActivity{

    private View timeLayout;
    private TextView timeTv;
    private EditText weightEt;
    private TimeSelector timeSelector;
    private TeaRecord record = new TeaRecord();

    private Long employeeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeeId = getIntent().getLongExtra("employeeId", -1);
        if (employeeId == -1){
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        setActionBarTitle(getString(R.string.add_tea_record));
        setActionMenuItem(0, R.drawable.ic_done_white, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (TextUtils.isEmpty(weightEt.getText().toString().trim())){
                    ToastUtil.warning("请输入茶叶重量！");
                    return true;
                }
                record.setWeight(Float.parseFloat(weightEt.getText().toString().trim()));
                record.setEmployeeId(employeeId);
                DBTeaRecordTools.getInstance().insert(record);
                ToastUtil.success("添加成功！");
                //同步刷新
                Intent intent = new Intent(BroadcastIntent.ACTION_SYNC_TEA_RECORD);
                sendBroadcast(intent);
                hideSoftKeyboard();
                finish();
                return true;
            }
        });
        timeLayout = findViewById(R.id.timeLayout);
        timeTv = (TextView) findViewById(R.id.timeTv);
        weightEt = (EditText) findViewById(R.id.weightEt);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelector = new TimeSelector(AddTeaRecordActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(Date date) {
                        record.setTime(date.getTime() + "");
                        String time = DateUtil.DateToString(date, DateStyle.YYYY_MM_DD);
                        timeTv.setText(time);
                    }
                }, "2017-01-01 00:00", "2020-12-01 00:00");
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.show();
            }
        });
        String time = DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD);
        timeTv.setText(time);
        record.setTime(System.currentTimeMillis() + "");
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_tea_record;
    }
}
