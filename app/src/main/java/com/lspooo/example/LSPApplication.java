package com.lspooo.example;

import android.app.Application;

import com.lspooo.plugin.common.LSPApplicationContext;
import com.lspooo.plugin.statistics.dao.StatisticsDao;
import com.yuntongxun.plugin.greendao3.helper.DaoHelper;


/**
 * Created by LSP on 2017/9/21.
 */

public class LSPApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        LSPApplicationContext.setContext(this);

        DaoHelper.initListener(new StatisticsDao());
        DaoHelper.initDAO(this);
    }
}
