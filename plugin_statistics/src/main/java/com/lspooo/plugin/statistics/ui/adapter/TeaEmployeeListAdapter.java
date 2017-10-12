package com.lspooo.plugin.statistics.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.common.adapter.BaseViewHolder;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;

import java.util.List;

/**
 * Created by LSP on 2017/10/11.
 */

public class TeaEmployeeListAdapter extends BaseQuickAdapter<TeaEmployee, BaseViewHolder>{

    public TeaEmployeeListAdapter(@LayoutRes int layoutResId, @Nullable List<TeaEmployee> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeaEmployee item) {
        TextView numberTv = helper.getView(R.id.numberTv);
        TextView nameTV = helper.getView(R.id.nameTv);
        if (item != null){
            numberTv.setText("" + item.getId());
            nameTV.setText(item.getName());
        }
    }
}
