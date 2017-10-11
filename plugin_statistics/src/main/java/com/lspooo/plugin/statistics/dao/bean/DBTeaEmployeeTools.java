package com.lspooo.plugin.statistics.dao.bean;

import com.yuntongxun.plugin.greendao3.helper.DaoHelper;
import com.yuntongxun.plugin.greendao3.helper.DaoMasterHelper;

import org.greenrobot.greendao.AbstractDao;

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
}
