package com.lspooo.plugin.statistics.dao;

import com.lspooo.plugin.statistics.dao.bean.TeaEmployeeDao;
import com.lspooo.plugin.statistics.dao.bean.TeaRecordDao;
import com.yuntongxun.plugin.greendao3.bean.IDao;
import com.yuntongxun.plugin.greendao3.bean.ISession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LSP on 2017/10/10.
 */

public class StatisticsDao implements IDao{
    @Override
    public void onCreate(Database db, boolean ifNotExists) {
        TeaEmployeeDao.createTable(db, ifNotExists);
        TeaRecordDao.createTable(db, ifNotExists);
    }

    @Override
    public void onDropTable(Database db, boolean ifExists) {
        TeaEmployeeDao.dropTable(db, ifExists);
        TeaRecordDao.dropTable(db, ifExists);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

    }

    @Override
    public List<Class<? extends AbstractDao<?, ?>>> getAbsDao() {
        List<Class<? extends AbstractDao<?, ?>>> list = new ArrayList<>();
        list.add(TeaEmployeeDao.class);
        list.add(TeaRecordDao.class);
        return list;
    }

    @Override
    public ISession getSession() {
        return new StatisticsSession();
    }
}
