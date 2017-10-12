package com.lspooo.plugin.statistics.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.lspooo.plugin.common.common.adapter.BaseQuickAdapter;
import com.lspooo.plugin.common.common.adapter.BaseViewHolder;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;

import java.util.List;

/**
 * Created by LSP on 2017/10/12.
 */

public class TeaRecordListAdapter extends BaseQuickAdapter<TeaRecord, BaseViewHolder>{

    public TeaRecordListAdapter(@LayoutRes int layoutResId, @Nullable List<TeaRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeaRecord item) {

    }
}
