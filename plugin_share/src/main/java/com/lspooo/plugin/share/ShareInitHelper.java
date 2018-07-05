package com.lspooo.plugin.share;

import android.content.Context;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lspooo on 2018/6/13.
 */

public class ShareInitHelper {

    public static final String WX_APP_ID = "wxc1c91efd5d6e5499";

    public static void initShare(Context context){
        //微信分享初始化
        registerWeChat(context);
    }

    /**
     * 微信分享初始化
     */
    public static void registerWeChat(Context context){
        IWXAPI api = WXAPIFactory.createWXAPI(context, WX_APP_ID, true);
        api.registerApp(WX_APP_ID);
    }
}
