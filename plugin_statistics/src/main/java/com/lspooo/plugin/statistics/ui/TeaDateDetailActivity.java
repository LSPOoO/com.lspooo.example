package com.lspooo.plugin.statistics.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

public class TeaDateDetailActivity extends CommonActivity {

    private RecyclerView mRecyclerView;
    private TeaRecordListAdapter mAdapter;
    private TextView totalCountTv;
    private TextView totalWeightTv;

    private String teaDate;
    private List<TeaRecord> teaRecordList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teaDate = getIntent().getStringExtra("teaDate");
        if (TextUtils.isEmpty(teaDate)) {
            finish();
            return;
        }
        initView();
        load();
    }

    private void initView() {
        String time = DateUtil.DateToString(DateUtil.StringToDate(teaDate, DateStyle.YYYYMMDD), DateStyle.YYYY_MM_DD);
        setActionBarTitle(time);
        totalWeightTv = (TextView) findViewById(R.id.totalWeightTv);
        totalCountTv = (TextView) findViewById(R.id.totalCountTv);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TeaRecordListAdapter(R.layout.layout_tea_record_item, teaRecordList, true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void load() {
        List<TeaRecord> result = DBTeaRecordTools.getInstance().queryTeaRecordByTime(teaDate);
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
        return R.layout.activity_tea_date_detail;
    }
}
