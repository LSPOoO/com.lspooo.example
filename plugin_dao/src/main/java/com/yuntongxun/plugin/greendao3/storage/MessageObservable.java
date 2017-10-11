/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package com.yuntongxun.plugin.greendao3.storage;


public class MessageObservable extends ECObservable<OnMessageChange> {
    private static final String TAG = MessageObservable.class.getSimpleName();
    private static MessageObservable mMessageObservable = null;

    static {
        synchronized (MessageObservable.class) {
            mMessageObservable = new MessageObservable();
        }
    }

    private MessageObservable() {
    }

    public static MessageObservable create() {
        return mMessageObservable;
    }

    /**
     * 分发数据库改变通知
     *
     * @param session
     */
    public void notifyChanged(String session) {
        notifyChanged(session, false);
    }

    /**
     * 通知消息改变
     *
     * @param session 会话
     * @param insert  是否新产生的消息
     */
    public void notifyChanged(String session, boolean insert) {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged(session, insert);
            }
        }
    }
}
