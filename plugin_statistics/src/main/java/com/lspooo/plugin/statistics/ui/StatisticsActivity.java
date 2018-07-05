package com.lspooo.plugin.statistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.tools.DateStyle;
import com.lspooo.plugin.common.tools.DateUtil;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.DBTeaEmployeeTools;
import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;
import com.lspooo.plugin.statistics.ui.adapter.EmployeeStatisticsAdapter;
import com.lspooo.plugin.statistics.ui.presenter.IStatisticsView;
import com.lspooo.plugin.statistics.ui.presenter.StatisticsPresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LSP on 2017/10/16.
 */

public class StatisticsActivity extends CommonActivity<IStatisticsView, StatisticsPresenter> implements IStatisticsView{

    private TextView bigFilterBtn;
    private View statisticsLayout;
    private TextView cancelBtn;
    private TextView startTimeTv;
    private TextView endTimeTv;
    private TextView totalWeightTv;
    private TextView totalCountTv;

    private RecyclerView mRecyclerView;
    private EmployeeStatisticsAdapter mAdapter;
    private List<TeaEmployee> employeeList = new ArrayList<>();
    private long filterStart = 0;
    private long filterEnd = 0;
    private boolean isFilter = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setActionBarTitle(getString(R.string.tea_statistics));
        bigFilterBtn = (TextView) findViewById(R.id.bigFilterBtn);
        bigFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsActivity.this, DateFilterSelectActivity.class);
                startActivityForResult(intent, 0x1);
            }
        });
        statisticsLayout = findViewById(R.id.statisticsLayout);
        cancelBtn = (TextView) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterStart = 0;
                filterEnd = 0;
                isFilter = false;
                initStatisticsLayout();
            }
        });
        startTimeTv = (TextView) findViewById(R.id.startTimeTv);
        endTimeTv = (TextView) findViewById(R.id.endTimeTv);
        totalCountTv = (TextView) findViewById(R.id.totalCountTv);
        totalWeightTv = (TextView) findViewById(R.id.totalWeightTv);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EmployeeStatisticsAdapter(R.layout.layout_employee_statistics, employeeList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position < 0 || position >= employeeList.size()) {
                    return;
                }
                TeaEmployee employee = employeeList.get(position);
                Intent intent = new Intent(StatisticsActivity.this, StatisticsDetailActivity.class);
                intent.putExtra("employeeId", employee.getId());
                intent.putExtra("filterStart", filterStart);
                intent.putExtra("filterEnd", filterEnd);
                intent.putExtra("employeeName", employee.getName());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStatisticsResult(List<TeaEmployee> result) {
        employeeList.clear();
        if (result != null && result.size() > 0) {
            employeeList.addAll(result);
        }
        mAdapter.notifyDataSetChanged();
        long sumCount = 0;
        Double sumWeight = 0.0;
        for (TeaEmployee employee : employeeList) {
            //总计数量与重量
            sumCount = sumCount + employee.getCount();
            BigDecimal a = new BigDecimal(String.valueOf(employee.getTotalWeight()));
            BigDecimal b = new BigDecimal(String.valueOf(sumWeight));
            BigDecimal c = b.add(a);
            sumWeight = c.doubleValue();
        }
        totalWeightTv.setText("采茶重量总计：" + sumWeight + "斤");
        totalCountTv.setText("采茶次数：" + sumCount + "次");
        initStatisticsLayout();
    }

    private void initStatisticsLayout(){

        if (isFilter){
            statisticsLayout.setVisibility(View.VISIBLE);
            bigFilterBtn.setVisibility(View.GONE);
            startTimeTv.setText(DateUtil.DateToString(DateUtil.StringToDate(filterStart + "", DateStyle.YYYYMMDD), DateStyle.YYYY_MM_DD));
            endTimeTv.setText(DateUtil.DateToString(DateUtil.StringToDate(filterEnd + "", DateStyle.YYYYMMDD), DateStyle.YYYY_MM_DD));
        } else {
            statisticsLayout.setVisibility(View.GONE);
            bigFilterBtn.setVisibility(View.VISIBLE);
        }
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
            mPresenter.startStatistics(filterStart + "", filterEnd + "");
        }
    }

    @Override
    public StatisticsPresenter getPresenter() {
        return new StatisticsPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_statistics;
    }
}
