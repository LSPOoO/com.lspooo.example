package com.lspooo.plugin.statistics.ui.presenter;

import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;

import java.util.List;

/**
 * Created by lspooo on 2018/1/25.
 */

public interface IStatisticsView {

    void onStatisticsResult(List<TeaEmployee> result);
}
