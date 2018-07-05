package com.lspooo.plugin.statistics.ui.presenter;

import com.lspooo.plugin.statistics.dao.bean.TeaDate;

import java.util.List;

/**
 * Created by lspooo on 2018/3/26.
 */

public interface ITeaDateView {

    void onLoadTeaDateResult(List<TeaDate> teaDateList);
}
