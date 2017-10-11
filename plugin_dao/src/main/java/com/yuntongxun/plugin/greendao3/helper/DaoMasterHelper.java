package com.yuntongxun.plugin.greendao3.helper;

import android.support.v4.util.LongSparseArray;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoMaster;

/**
 * Created by WJ on 2016/12/3.
 */

public class DaoMasterHelper {
    private static DaoMasterHelper mInstance;

    private DaoMasterHelper() {
    }

    public static DaoMasterHelper getInstance() {
        if (mInstance == null) {
            synchronized (DaoMasterHelper.class) {
                mInstance = new DaoMasterHelper();
            }
        }
        return mInstance;
    }

    private AbstractDaoMaster daoMaster;
    private LongSparseArray<DaoHelper<?>> array = new LongSparseArray<DaoHelper<?>>();


    public void addCrudHelper(DaoHelper<?> c) {
        array.append(array.size(), c);
    }

    public void initDaoMaster(AbstractDaoMaster daoMaster) {
        this.daoMaster = daoMaster;
    }

    public void resetDAO() {
        daoMaster = null;
        int size = array.size();
        if (size == 0) {
            return;
        }
        for (int index = 0; index < size; index++) {
            DaoHelper<?> daoHelper = array.get(index);
            if(daoHelper != null) {
                daoHelper.resetDao();
            }

        }
        array.clear();
    }

    public AbstractDao getDao(Class<?> c) {
        if (daoMaster != null) {
            return daoMaster.newSession().getDao(c);
        }
        return null;
    }

}
