package com.lspooo.example;

import android.app.Application;

import com.lspooo.example.plugin.common.LSPApplicationContext;

/**
 * Created by LSP on 2017/9/21.
 */

public class LSPApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        LSPApplicationContext.setContext(this);
    }
}
