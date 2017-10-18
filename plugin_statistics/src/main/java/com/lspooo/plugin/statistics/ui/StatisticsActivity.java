package com.lspooo.plugin.statistics.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.DBTeaRecordTools;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;
import com.lspooo.plugin.statistics.ui.adapter.TeaRecordListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LSP on 2017/10/16.
 */

public class StatisticsActivity extends CommonActivity{

    private RecyclerView mRecyclerView;
    private TeaRecordListAdapter mAdapter;
    private List<TeaRecord> recordList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        loadAllTeaRecord();
    }

    private void initView() {
        setActionBarTitle(getString(R.string.tea_statistics));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new TeaRecordListAdapter(R.layout.layout_tea_record_item, recordList, true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadAllTeaRecord() {
        recordList.clear();
        List<TeaRecord> result = DBTeaRecordTools.getInstance().queryTeaRecord();
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
        return R.layout.activity_statistics;
    }
}
