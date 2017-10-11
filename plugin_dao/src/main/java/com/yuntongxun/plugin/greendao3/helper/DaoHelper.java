package com.yuntongxun.plugin.greendao3.helper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yuntongxun.plugin.greendao3.bean.DaoMaster;
import com.yuntongxun.plugin.greendao3.bean.IDao;
import com.yuntongxun.plugin.greendao3.storage.MessageObservable;
import com.yuntongxun.plugin.greendao3.storage.OnMessageChange;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * Created by WJ on 2016/12/3.
 * 简单的封装一下增、删、改、查
 */

public abstract class DaoHelper<T> {
    private static final String TAG = DaoHelper.class.getSimpleName();
    protected AbstractDao dao;

    protected abstract AbstractDao initDao();

    protected abstract void resetDao();

    public DaoHelper() {
        dao = initDao();
        DaoMasterHelper.getInstance().addCrudHelper(this);
    }

    /**
     * 单个对象插入
     *
     * @param t 默认replace 为 true
     * @return
     */
    public long insert(T t) {
        return insert(t, true);
    }

    /**
     * 单个对象插入 可替换存在内容
     *
     * @param t
     * @param replace true 如存在则替换 false 直接插入
     * @return
     */
    public long insert(T t, boolean replace) {
        if (dao == null) {
            return -1;
        }
        if (replace) {
            return dao.insertOrReplace(t);
        }
        return dao.insert(t);
    }

    /**
     * 插入集合
     *
     * @param list
     */
    public void insert(List<T> list) {
        insert(list, false);
    }

    /**
     * 插入集合 可替换存在内容
     *
     * @param list
     * @param replace true 如存在则替换 false 直接插入
     */
    public void insert(List<T> list, boolean replace) {
        if (dao == null) {
            return;
        }
        if (replace) {
            dao.insertOrReplaceInTx(list);
        } else {
            dao.insertInTx(list);
        }
    }

    /**
     * 删除一个对象
     *
     * @param t
     */
    public void delete(T t) {
        if (dao == null) {
            return;
        }
        dao.delete(t);
    }

    /**
     * 删除多个对象
     *
     * @param list
     */
    public void delete(List<T> list) {
        if (dao == null) {
            return;
        }
        dao.deleteInTx(list);
    }

    /**
     * 删除表中全部数据 谨慎调用
     */
    public void deleteAll() {
        if (dao == null) {
            return;
        }
        dao.deleteAll();
    }

    /**
     * 修改单个对象
     *
     * @param t
     */
    public void update(T t) {
        if (dao == null) {
            return;
        }
        dao.update(t);
    }

    /**
     * 修改多个对象
     *
     * @param list
     */
    public void update(List<T> list) {
        if (dao == null) {
            return;
        }
        dao.updateInTx(list);
    }

    /**
     * 查询全部内容
     *
     * @return
     */
    public List<T> query() {
        if (dao == null) {
            return null;
        }
        return dao.queryBuilder().list();
    }

    /**
     * 查询全部内容 可排序
     *
     * @param p
     * @param asc
     * @return
     */
    public List<T> query(Property p, boolean asc) {
        if (dao == null) {
            return null;
        }
        if (asc) {
            return dao.queryBuilder().orderAsc(p).list();
        } else {
            return dao.queryBuilder().orderDesc(p).list();
        }
    }

    /**
     * 带条件查询
     *
     * @param where
     * @return
     */
    public List<T> query(WhereCondition where) {
        if (dao == null) {
            return null;
        }
        return dao.queryBuilder().where(where).list();
    }

    /**
     * 多条件查询
     *
     * @param where1
     * @param where2
     * @return
     */
    public List<T> query(WhereCondition where1, WhereCondition... where2) {
        if (dao == null) {
            return null;
        }
        return dao.queryBuilder().where(where1, where2).list();
    }

    /**
     * 带条件查询 or
     *
     * @param cond1
     * @param cond2
     * @param cond3
     * @return
     */
    public List<T> queryOr(WhereCondition cond1, WhereCondition cond2, WhereCondition... cond3) {
        if (dao == null) {
            return null;
        }
        return dao.queryBuilder().whereOr(cond1, cond2, cond3).list();
    }

    /**
     * 带条件插件并且排序
     *
     * @param where
     * @param p
     * @param asc
     * @return
     */
    public List<T> query(WhereCondition where, Property p, boolean asc) {
        if (dao == null) {
            return null;
        }
        if (asc) {
            return dao.queryBuilder().where(where).orderAsc(p).list();
        }
        return dao.queryBuilder().where(where).orderDesc(p).list();
    }

    public List<T> queryOr(WhereCondition w1, WhereCondition w2, Property p, boolean asc) {
        if (dao == null) {
            return null;
        }
        if (asc) {
            return dao.queryBuilder().whereOr(w1, w2).orderAsc(p).list();
        }
        return dao.queryBuilder().whereOr(w1, w2).orderDesc(p).list();
    }

    public long getCount() {
        if (dao == null) {
            return 0;
        }
        return dao.count();
    }

    public AbstractDao<T, ?> getDao() {
        return dao;
    }

    private final MessageObservable mMsgObservable = MessageObservable.create();

    public void registerObserver(OnMessageChange observer) {
        mMsgObservable.registerObserver(observer);
    }

    public void unregisterObserver(OnMessageChange observer) {
        mMsgObservable.unregisterObserver(observer);
    }

    protected void notifyChanged(String session) {
        notifyChanged(session, false);
    }

    protected void notifyChanged(String session, boolean insert) {
        mMsgObservable.notifyChanged(session, insert);
    }


    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = false;

    /**
     * 必须放在Applicaiton onCreate方法中进行且 只能调用一次
     *
     * @param iDaos 所有模块的表结构
     * @see #initDAO(Context) 必须放在创建数据库之前
     */
    public static void initListener(IDao... iDaos) {
        // 初始化基本数据库
        if (iDaos == null || iDaos.length <= 0) {
            return;
        }
        DaoMaster.clearAllDao();
        for (IDao iDao : iDaos) {
            DaoMaster.addDaoListener(iDao);
        }
    }

    public static void initDAO(Context context) {
        resetDAO();

        //数据库加密 ENCRYPTED为true表示加密 false表示不加密
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, ENCRYPTED ? "RX_im_encrypted_db" : "RX_im_db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoMasterHelper.getInstance().initDaoMaster(daoMaster);
    }

    /**
     * 释放数据库资源
     */
    public static void resetDAO() {
        DaoMasterHelper.getInstance().resetDAO();
    }



}
