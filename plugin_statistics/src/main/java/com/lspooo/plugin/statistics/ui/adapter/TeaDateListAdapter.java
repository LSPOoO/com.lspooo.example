package com.lspooo.plugin.statistics.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.common.adapter.BaseViewHolder;
import com.lspooo.plugin.common.tools.DateStyle;
import com.lspooo.plugin.common.tools.DateUtil;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.TeaDate;

import java.util.List;

/**
 * Created by lspooo on 2018/3/26.
 */

public class TeaDateListAdapter extends BaseQuickAdapter<TeaDate, BaseViewHolder> {

    public TeaDateListAdapter(@LayoutRes int layoutResId, @Nullable List<TeaDate> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeaDate item) {
        TextView timeTv = helper.getView(R.id.timeTv);
        TextView descriptionTv = helper.getView(R.id.descriptionTv);
        if (item != null) {
            timeTv.setText(DateUtil.DateToString(DateUtil.StringToDate(item.getTime(), DateStyle.YYYYMMDD), DateStyle.YYYY_MM_DD));
            descriptionTv.setText("采茶总量总计：" + item.getWeight() + "斤，采茶次数：" + item.getTotalCount() + "次");
        }
    }
}
