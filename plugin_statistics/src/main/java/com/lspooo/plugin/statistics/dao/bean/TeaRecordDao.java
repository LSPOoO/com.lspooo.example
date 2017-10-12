package com.lspooo.plugin.statistics.dao.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/**
 * Created by LSP on 2017/10/12.
 */

public class TeaRecordDao extends AbstractDao<TeaRecord, Long>{


    public static final String TABLENAME = "TEA_RECORD";

    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property EmployeeId = new Property(1, Long.class, "employeeId", false, "EMPLOYEE_ID");
        public final static Property Weight = new Property(2, Float.class, "weight", false, "WEIGHT");
        public final static Property Time = new Property(3, String.class, "time", false, "TIME");
    }

    public TeaRecordDao(DaoConfig config) {
        super(config);
    }

    public TeaRecordDao(DaoConfig config, AbstractDaoSession daoSession) {
        super(config, daoSession);
    }


    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TEA_RECORD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT," + // 0: _id
                "\"EMPLOYEE_ID\" INTEGER," + // 1: employeeId
                "\"WEIGHT\" FLOAT," + // 2: weight
                "\"TIME\" TEXT);"); // 3: time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TEA_RECORD\"";
        db.execSQL(sql);
    }

    @Override
    protected TeaRecord readEntity(Cursor cursor, int offset) {
        TeaRecord record = new TeaRecord(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0),
                cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1),
                cursor.isNull(offset + 2) ? null : cursor.getFloat(offset + 2),
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        return record;
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    protected void readEntity(Cursor cursor, TeaRecord entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEmployeeId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setWeight(cursor.isNull(offset + 2) ? null : cursor.getFloat(offset + 2));
        entity.setTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
    }

    @Override
    protected void bindValues(DatabaseStatement stmt, TeaRecord entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null){
            stmt.bindLong(1, id);
        }

        Long employeeId = entity.getEmployeeId();
        if (employeeId != null){
            stmt.bindLong(2, employeeId);
        }

        Float weight = entity.getWeight();
        if (weight != null){
            stmt.bindDouble(3, weight);
        }

        String time = entity.getTime();
        if (time != null){
            stmt.bindString(4, time);
        }
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, TeaRecord entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null){
            stmt.bindLong(1, id);
        }

        Long employeeId = entity.getEmployeeId();
        if (employeeId != null){
            stmt.bindLong(2, employeeId);
        }

        Float weight = entity.getWeight();
        if (weight != null){
            stmt.bindDouble(3, weight);
        }

        String time = entity.getTime();
        if (time != null){
            stmt.bindString(4, time);
        }
    }

    @Override
    protected Long updateKeyAfterInsert(TeaRecord entity, long rowId) {
        return entity.getId();
    }

    @Override
    protected Long getKey(TeaRecord entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected boolean hasKey(TeaRecord entity) {
        return entity.getId() != null;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
}
