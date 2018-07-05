package com.lspooo.plugin.statistics.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.tools.DateStyle;
import com.lspooo.plugin.common.tools.DateUtil;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.DBTeaRecordTools;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;
import com.lspooo.plugin.statistics.ui.adapter.TeaRecordListAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lspooo on 2018/3/26.
 */

public class StatisticsDetailActivity extends CommonActivity{

    private RecyclerView mRecyclerView;
    private TeaRecordListAdapter mAdapter;
    private TextView totalCountTv;
    private TextView totalWeightTv;
    private TextView startTimeTv;
    private TextView endTimeTv;

    private long employeeId;
    private String employeeName;
    private long filterStart;
    private long filterEnd;
    private List<TeaRecord> teaRecordList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeeId = getIntent().getLongExtra("employeeId", 0);
        filterStart = getIntent().getLongExtra("filterStart", 0);
        filterEnd = getIntent().getLongExtra("filterEnd", 0);
        employeeName = getIntent().getStringExtra("employeeName");
        if (employeeId == 0 || filterStart == 0 || filterEnd == 0) {
            finish();
            return;
        }
        initView();
        load();
    }

    private void initView() {
        setActionBarTitle(employeeName);
        totalWeightTv = (TextView) findViewById(R.id.totalWeightTv);
        totalCountTv = (TextView) findViewById(R.id.totalCountTv);
        startTimeTv = (TextView) findViewById(R.id.startTimeTv);
        endTimeTv = (TextView) findViewById(R.id.endTimeTv);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TeaRecordListAdapter(R.layout.layout_tea_record_item, teaRecordList, false);
        mRecyclerView.setAdapter(mAdapter);

        startTimeTv.setText(DateUtil.DateToString(DateUtil.StringToDate(filterStart + "", DateStyle.YYYYMMDD), DateStyle.YYYY_MM_DD));
        endTimeTv.setText(DateUtil.DateToString(DateUtil.StringToDate(filterEnd + "", DateStyle.YYYYMMDD), DateStyle.YYYY_MM_DD));
    }

    private void load() {
        List<TeaRecord> result = DBTeaRecordTools.getInstance().queryTeaRecord(filterStart + "", filterEnd + "", employeeId);

        teaRecordList.clear();
        if (result != null && result.size() > 0) {
            teaRecordList.addAll(result);
        }
        mAdapter.notifyDataSetChanged();

        Double sum = 0.0;
        for (TeaRecord record : teaRecordList){
            BigDecimal a = new BigDecimal(String.valueOf(record.getWeight()));
            BigDecimal b = new BigDecimal(String.valueOf(sum));
            BigDecimal c = b.add(a);
            sum = c.doubleValue();
        }
        totalWeightTv.setText("采茶重量总计：" + sum + "斤");
        totalCountTv.setText("采茶次数：" + teaRecordList.size() + "次");
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_statistics_detail;
    }
}
