package com.yuntongxun.plugin.greendao3.bean;


import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

import java.util.List;


/**
 * Created by ruijie on 2016/11/21.
 */

public interface IDao {

    void onCreate(Database db, boolean ifNotExists);

    void onDropTable(Database db, boolean ifExists);

    void onUpgrade(Database db, int oldVersion, int newVersion);

    List<Class<? extends AbstractDao<?, ?>>> getAbsDao();

    ISession getSession();


}
