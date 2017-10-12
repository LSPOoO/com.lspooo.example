package com.lspooo.plugin.statistics.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.common.view.TimeSelector;
import com.lspooo.plugin.statistics.R;

import java.util.Date;

/**
 * Created by LSP on 2017/10/12.
 */

public class AddTeaRecordActivity extends CommonActivity{

    private View timeLayout;
    private TimeSelector timeSelector;

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
                return true;
            }
        });

        timeLayout = findViewById(R.id.timeLayout);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelector = new TimeSelector(AddTeaRecordActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(Date time) {

                    }
                }, "2017-01-01 00:00", "2100-12-31 00:00");
                timeSelector.show();
            }
        });
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
