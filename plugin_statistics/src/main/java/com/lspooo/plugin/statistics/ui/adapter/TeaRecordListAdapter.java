package com.lspooo.plugin.statistics.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.common.adapter.BaseViewHolder;
import com.lspooo.plugin.common.tools.DateStyle;
import com.lspooo.plugin.common.tools.DateUtil;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;

import java.util.Date;
import java.util.List;

/**
 * Created by LSP on 2017/10/12.
 */

public class TeaRecordListAdapter extends BaseQuickAdapter<TeaRecord, BaseViewHolder>{

    private boolean showName;

    public TeaRecordListAdapter(@LayoutRes int layoutResId, @Nullable List<TeaRecord> data, boolean showName) {
        super(layoutResId, data);
        this.showName = showName;
    }

    @Override
    protected void convert(BaseViewHolder helper, TeaRecord item) {
        TextView weightTv = helper.getView(R.id.weightTv);
        TextView timeTv = helper.getView(R.id.timeTv);
        TextView nameTv = helper.getView(R.id.nameTv);
        if (item != null){
            weightTv.setText("重量：" + item.getWeight() + "公斤");
            timeTv.setText(DateUtil.DateToString(new Date(Long.parseLong(item.getTime())), DateStyle.YYYY_MM_DD));
            if (showName){
                nameTv.setText(item.getName());
                nameTv.setVisibility(View.VISIBLE);
            } else{
                nameTv.setVisibility(View.GONE);
            }
        }
    }
}
