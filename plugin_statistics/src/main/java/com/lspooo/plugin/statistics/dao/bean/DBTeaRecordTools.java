package com.lspooo.plugin.statistics.dao.bean;

import android.database.Cursor;
import android.text.TextUtils;

import com.yuntongxun.plugin.greendao3.helper.DaoHelper;
import com.yuntongxun.plugin.greendao3.helper.DaoMasterHelper;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.WhereCondition;
import org.w3c.dom.Text;

import java.util.ArrayList;
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

    public List<TeaRecord> queryTeaRecord(long id, String startTime, String endTime){
        if (dao == null) {
            return null;
        }
        WhereCondition where = TeaRecordDao.Properties.EmployeeId.eq(id);
        WhereCondition where2 = TeaRecordDao.Properties.Time.between(startTime, endTime);
        List<TeaRecord> list = dao.queryBuilder().where(where, where2).list();
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    public List<TeaRecord> queryTeaRecord(String startTime, String endTime){
        String where;
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)){
            where = "";
        } else{
            where = " WHERE " + TeaRecordDao.TABLENAME + "." + TeaRecordDao.Properties.Time.columnName + " <= '" + endTime + "' AND " +
                    TeaRecordDao.TABLENAME + "." + TeaRecordDao.Properties.Time.columnName + " >= '" + startTime + "' ";
        }
        String sql = "SELECT * FROM " +
                TeaRecordDao.TABLENAME + " LEFT OUTER JOIN " + TeaEmployeeDao.TABLENAME +
                " ON " + TeaRecordDao.TABLENAME + "." + TeaRecordDao.Properties.EmployeeId.columnName + " = " + TeaEmployeeDao.TABLENAME + "." + TeaEmployeeDao.Properties.Id.columnName +
                where +
                " ORDER BY " + TeaRecordDao.TABLENAME + "." + TeaRecordDao.Properties.EmployeeId.columnName + " ASC, " + TeaRecordDao.TABLENAME + "." + TeaRecordDao.Properties.Id.columnName + " ASC;";
        Cursor cursor = dao.getSession().getDatabase().rawQuery(sql, null);
        List<TeaRecord> recordList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                TeaRecord teaRecord = new TeaRecord();
                teaRecord.setId(cursor.getLong(cursor.getColumnIndex(TeaRecordDao.Properties.Id.columnName)));
                teaRecord.setEmployeeId(cursor.getLong(cursor.getColumnIndex(TeaRecordDao.Properties.EmployeeId.columnName)));
                teaRecord.setWeight(cursor.getFloat(cursor.getColumnIndex(TeaRecordDao.Properties.Weight.columnName)));
                teaRecord.setTime(cursor.getString(cursor.getColumnIndex(TeaRecordDao.Properties.Time.columnName)));
                teaRecord.setName(cursor.getString(cursor.getColumnIndex(TeaEmployeeDao.Properties.Name.columnName)));
                recordList.add(teaRecord);
            }
        }
        return recordList;
    }

    public List<TeaRecord> queryTeaRecord(){
        return queryTeaRecord("", "");
    }

}
