package com.lspooo.plugin.statistics.dao.bean;

import com.yuntongxun.plugin.greendao3.helper.DaoHelper;
import com.yuntongxun.plugin.greendao3.helper.DaoMasterHelper;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * Created by LSP on 2017/10/10.
 */

public class DBTeaEmployeeTools extends DaoHelper<TeaEmployee>{

    private static DBTeaEmployeeTools mInstance;

    private DBTeaEmployeeTools() {}

    public static DBTeaEmployeeTools getInstance() {
        if (mInstance == null) {
            synchronized (DBTeaEmployeeTools.class) {
                mInstance = new DBTeaEmployeeTools();
            }
        }
        return mInstance;
    }

    @Override
    protected AbstractDao initDao() {
        return DaoMasterHelper.getInstance().getDao(TeaEmployee.class);
    }

    @Override
    protected void resetDao() {
        mInstance = null;
    }

    public TeaEmployee queryTeaEmployee(long id){
        if (dao == null) {
            return null;
        }
        WhereCondition where = TeaEmployeeDao.Properties.Id.eq(id);
        List<TeaEmployee> list = dao.queryBuilder().where(where).list();
        if (list != null && list.size() > 0) {
            TeaEmployee employee = list.get(0);
            return employee;
        }
        return null;
    }
}
