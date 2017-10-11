package com.yuntongxun.plugin.greendao3.storage;


public interface OnMessageChange {
    /**
     * 数据库改变
     * @param sessionId
     * @param insert 是否有新消息
     */
    void onChanged(String sessionId  , boolean insert);
}
