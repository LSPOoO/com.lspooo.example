package com.yuntongxun.plugin.greendao3.bean;


import org.greenrobot.greendao.AbstractDao;

import java.util.List;


/**
 * Created by ruijie on 2016/11/21.
 */

public interface ISession {
    List<Class> getEntityClass();
    List<AbstractDao> getSessionDao();
    void init();
}
