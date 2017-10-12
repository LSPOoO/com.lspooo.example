package com.lspooo.plugin.statistics.dao;

import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;
import com.lspooo.plugin.statistics.dao.bean.TeaEmployeeDao;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;
import com.lspooo.plugin.statistics.dao.bean.TeaRecordDao;
import com.yuntongxun.plugin.greendao3.bean.DaoSession;
import com.yuntongxun.plugin.greendao3.bean.ISession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LSP on 2017/10/10.
 */

public class StatisticsSession implements ISession{

    private TeaEmployeeDao teaEmployeeDao;
    private TeaRecordDao teaRecordDao;

    private DaoConfig teaEmployeeDaoConfig;
    private DaoConfig teaRecordDaoConfig;

    @Override
    public List<Class> getEntityClass() {
        List<Class> list = new ArrayList<>();
        list.add(TeaEmployee.class);
        list.add(TeaRecord.class);
        return list;
    }

    @Override
    public List<AbstractDao> getSessionDao() {
        List<AbstractDao> list =new ArrayList<>();
        list.add(teaEmployeeDao);
        list.add(teaRecordDao);
        return list;
    }

    @Override
    public void init() {
        DaoSession.setDaoSessionListener(new DaoSession.NNDaoSession() {
            @Override
            public void setNDaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap, DaoSession ad) {

                teaEmployeeDaoConfig = daoConfigMap.get(TeaEmployeeDao.class);
                teaEmployeeDaoConfig.initIdentityScope(type);

                teaRecordDaoConfig = daoConfigMap.get(TeaRecordDao.class);
                teaRecordDaoConfig.initIdentityScope(type);

                teaEmployeeDao = new TeaEmployeeDao(teaEmployeeDaoConfig, ad);
                teaRecordDao = new TeaRecordDao(teaRecordDaoConfig, ad);
            }

            @Override
            public void setNClear() {
                teaEmployeeDaoConfig.getIdentityScope().clear();
                teaRecordDaoConfig.getIdentityScope().clear();
            }
        });
    }
}
