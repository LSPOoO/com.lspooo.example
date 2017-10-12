package com.lspooo.plugin.statistics.dao.bean;

import com.yuntongxun.plugin.greendao3.helper.DaoHelper;
import com.yuntongxun.plugin.greendao3.helper.DaoMasterHelper;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * Created by LSP on 2017/10/10.
 */

public class DBTeaRecordTools extends DaoHelper<TeaRecord>{

    private static DBTeaRecordTools mInstance;

    private DBTeaRecordTools() {}

    public static DBTeaRecordTools getInstance() {
        if (mInstance == null) {
            synchronized (DBTeaRecordTools.class) {
                mInstance = new DBTeaRecordTools();
            }
        }
        return mInstance;
    }

    @Override
    protected AbstractDao initDao() {
        return DaoMasterHelper.getInstance().getDao(TeaRecord.class);
    }

    @Override
    protected void resetDao() {
        mInstance = null;
    }

    public List<TeaRecord> queryTeaRecord(long id){
        if (dao == null) {
            return null;
        }
        WhereCondition where = TeaRecordDao.Properties.EmployeeId.eq(id);
        List<TeaRecord> list = dao.queryBuilder().where(where).list();
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    public void queryTeaRecord(String id, String startTime, String endTime){


        String sql = "";
    }

}
