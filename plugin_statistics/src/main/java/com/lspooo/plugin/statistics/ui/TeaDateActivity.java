package com.lspooo.plugin.statistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.TeaDate;
import com.lspooo.plugin.statistics.ui.adapter.TeaDateListAdapter;
import com.lspooo.plugin.statistics.ui.presenter.ITeaDateView;
import com.lspooo.plugin.statistics.ui.presenter.TeaDatePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lspooo on 2018/3/26.
 */

public class TeaDateActivity extends CommonActivity<ITeaDateView, TeaDatePresenter> implements ITeaDateView{

    private RecyclerView mRecyclerView;
    private TeaDateListAdapter mAdapter;

    private List<TeaDate> teaDateList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mPresenter.startListTeaDate();
    }

    private void initView() {
        setActionBarTitle(getString(R.string.tea_date));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TeaDateListAdapter(R.layout.layout_tea_date_item, teaDateList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position <0 || position >= teaDateList.size()) {
                    return;
                }
                TeaDate teaDate = teaDateList.get(position);
                Intent intent = new Intent(TeaDateActivity.this, TeaDateDetailActivity.class);
                intent.putExtra("teaDate", teaDate.getTime());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public TeaDatePresenter getPresenter() {
        return new TeaDatePresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tea_date;
    }

    @Override
    public void onLoadTeaDateResult(List<TeaDate> result) {
        teaDateList.clear();
        if (result != null && result.size() > 0) {
            teaDateList.addAll(result);
        }
        mAdapter.notifyDataSetChanged();
    }
}
