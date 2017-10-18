package com.lspooo.plugin.statistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.tools.DateStyle;
import com.lspooo.plugin.common.tools.DateUtil;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.common.view.TimeSelector;
import com.lspooo.plugin.statistics.R;

import java.util.Date;

/**
 * Created by LSP on 2017/10/17.
 */

public class DateFilterSelectActivity extends CommonActivity{

    private View startTimeLayout;
    private TextView startTimeTv;
    private View endTimeLayout;
    private TextView endTimeTv;

    private long startDate;
    private long endDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startDate = getIntent().getLongExtra("startDate", -1);
        endDate = getIntent().getLongExtra("endDate", -1);
        if (startDate == -1 || endDate == -1){
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        setActionBarTitle("筛选");
        setActionMenuItem(0, R.drawable.ic_done_white, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                intent.putExtra("startDate", startDate);
                intent.putExtra("endDate", endDate);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
        });
        startTimeLayout = findViewById(R.id.startTimeLayout);
        startTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSelector timeSelector = new TimeSelector(DateFilterSelectActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(Date date) {
                        startDate = date.getTime();
                        startTimeTv.setText(DateUtil.DateToString(date, DateStyle.YYYY_MM_DD));
                    }
                }, "2017-01-01 00:00", "2020-12-01 00:00");
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.show();
            }
        });
        startTimeTv = (TextView) findViewById(R.id.startTimeTv);
        endTimeLayout = findViewById(R.id.endTimeLayout);
        endTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSelector timeSelector = new TimeSelector(DateFilterSelectActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(Date date) {
                        endDate = date.getTime();
                        endTimeTv.setText(DateUtil.DateToString(date, DateStyle.YYYY_MM_DD));
                    }
                }, "2017-01-01 00:00", "2020-12-01 00:00");
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.show();
            }
        });
        endTimeTv = (TextView) findViewById(R.id.endTimeTv);

        startTimeTv.setText(DateUtil.DateToString(new Date(startDate), DateStyle.YYYY_MM_DD));
        endTimeTv.setText(DateUtil.DateToString(new Date(endDate), DateStyle.YYYY_MM_DD));
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_date_filter;
    }
}
