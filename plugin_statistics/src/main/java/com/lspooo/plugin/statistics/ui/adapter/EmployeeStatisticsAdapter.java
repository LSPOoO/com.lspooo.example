package com.lspooo.plugin.statistics.ui.adapter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.common.adapter.BaseViewHolder;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;

import java.util.List;

/**
 * Created by lspooo on 2018/1/18.
 */

public class EmployeeStatisticsAdapter extends BaseQuickAdapter<TeaEmployee, BaseViewHolder>{

    public EmployeeStatisticsAdapter(@LayoutRes int layoutResId, @Nullable List<TeaEmployee> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeaEmployee item) {
        TextView employeeNameTv = helper.getView(R.id.employeeNameTv);
        TextView descriptionTv = helper.getView(R.id.descriptionTv);
        if (item != null) {
            employeeNameTv.setText(item.getId() + "." + item.getName());
            descriptionTv.setText("采茶总量总计：" + item.getTotalWeight() + "斤，采茶次数：" + item.getCount() + "次");
        }
    }
}
