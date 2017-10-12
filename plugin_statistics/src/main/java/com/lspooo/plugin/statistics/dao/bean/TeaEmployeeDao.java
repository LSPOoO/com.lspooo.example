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
 * Created by LSP on 2017/10/10.
 */

public class TeaEmployeeDao extends AbstractDao<TeaEmployee, Long>{

    public static final String TABLENAME = "TEA_EMPLOYEE";

    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
    }

    public TeaEmployeeDao(DaoConfig config) {
        super(config);
    }

    public TeaEmployeeDao(DaoConfig config, AbstractDaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TEA_EMPLOYEE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT," + // 0: _id
                "\"NAME\" TEXT);"); // 2: name
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TEA_EMPLOYEE\"";
        db.execSQL(sql);
    }

    @Override
    protected TeaEmployee readEntity(Cursor cursor, int offset) {
        TeaEmployee employee = new TeaEmployee(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0),
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        return employee;
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    protected void readEntity(Cursor cursor, TeaEmployee entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
    }

    @Override
    protected void bindValues(DatabaseStatement stmt, TeaEmployee entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null){
            stmt.bindLong(1, id);
        }

        String name = entity.getName();
        if (name != null){
            stmt.bindString(2, name);
        }
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, TeaEmployee entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null){
            stmt.bindLong(1, id);
        }

        String name = entity.getName();
        if (name != null){
            stmt.bindString(2, name);
        }
    }

    @Override
    protected Long updateKeyAfterInsert(TeaEmployee entity, long rowId) {
        return entity.getId();
    }

    @Override
    protected Long getKey(TeaEmployee entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected boolean hasKey(TeaEmployee entity) {
        return entity.getId() != null;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
}
