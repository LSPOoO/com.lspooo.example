package com.lspooo.plugin.common;

import android.content.Context;

/**
 * Created by LSP on 2017/9/23.
 */

public class LSPApplicationContext {

    private static Context mContext;
    private static String mPackageName;

    public static void setContext(Context context) {
        mContext = context;
        if (mContext == null) {
            return;
        }
        mPackageName = context.getPackageName();
    }

    public static Context getContext() {
        return mContext;
    }
}
