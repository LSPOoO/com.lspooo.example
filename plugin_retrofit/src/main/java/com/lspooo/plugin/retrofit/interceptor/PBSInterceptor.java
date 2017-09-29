package com.lspooo.plugin.retrofit.interceptor;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Date;

import okhttp3.Interceptor;

/**
 * Created by LSP on 2017/9/29.
 */

public class PBSInterceptor implements Interceptor {

    private PBSInterceptor() {
    }

    private static PBSInterceptor instance;

    public static PBSInterceptor getInstance() {
        if (instance == null) {
            synchronized (PBSInterceptor.class) {
                instance = new PBSInterceptor();
            }
        }
        return instance;
    }

    private String auth;

    public void setAuth(String auth) {
        this.auth = auth;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        long timeMillis = System.currentTimeMillis();
        return chain.proceed(chain.request().newBuilder()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
//                .addHeader("Authorization", TextUtils.isEmpty(auth) ? AppMgr.getRequestAuth(timeMillis) : auth)
                .addHeader("Date", new Date(timeMillis).toGMTString())
                .addHeader("Connection", "close")
                .build());
    }
}
