package com.lspooo.plugin.share;

import android.content.Context;
import android.graphics.Bitmap;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by lspooo on 2018/6/13.
 */

public class WXShareUtils {

    /**
     * 分享到微信聊天
     * @param context
     * @param shareBean
     */
    public static void shareToWXSceneSession(Context context, ShareBean shareBean) {
        switch (shareBean.getType()) {
            case TXT:
                shareTextToWXSceneSession(context, shareBean.getContent());
                break;
            case IMAGE:
                shareImageToWXSceneSession(context, shareBean.getBitmap());
                break;
            default:
                break;
        }
    }

    /**
     * 分享到微信朋友圈
     * @param context
     * @param shareBean
     */
    public static void shareToWXSceneTimeline(Context context, ShareBean shareBean) {
        switch (shareBean.getType()) {
            case TXT:
                shareTextToWXSceneTimeline(context, shareBean.getContent());
                break;
            case IMAGE:
                shareImageToWXSceneTimeline(context, shareBean.getBitmap());
                break;
            default:
                break;
        }
    }

    /**
     * 分享文字到微信聊天
     * @param context
     * @param text
     */
    public static void shareTextToWXSceneSession(Context context, String text){
        shareTextToWX(context, text, SendMessageToWX.Req.WXSceneSession);
    }

    /**
     * 分享文字到微信朋友圈
     * @param context
     * @param text
     */
    public static void shareTextToWXSceneTimeline(Context context, String text){
        shareTextToWX(context, text, SendMessageToWX.Req.WXSceneTimeline);
    }

    /**
     * 分享文字到微信
     * @param context
     * @param text
     * @param targetScene
     */
    public static void shareTextToWX(Context context, String text, int targetScene){
        IWXAPI api = WXAPIFactory.createWXAPI(context, ShareInitHelper.WX_APP_ID);
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "text";
        req.message = msg;
        req.scene = targetScene;
        api.sendReq(req);
    }

    /**
     * 分享图片到微信聊天
     * @param context
     * @param bitmap
     */
    public static void shareImageToWXSceneSession(Context context, Bitmap bitmap){
        shareImageToWX(context, bitmap, SendMessageToWX.Req.WXSceneSession);
    }

    /**
     * 分享图片到微信朋友圈
     * @param context
     * @param bitmap
     */
    public static void shareImageToWXSceneTimeline(Context context, Bitmap bitmap){
        shareImageToWX(context, bitmap, SendMessageToWX.Req.WXSceneTimeline);
    }

    /**
     * 分享图片到微信
     * @param context
     * @param bitmap
     * @param targetScene
     */
    private static void shareImageToWX(Context context, Bitmap bitmap, int targetScene) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, ShareInitHelper.WX_APP_ID);
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        bitmap.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "image";
        req.message = msg;
        req.scene = targetScene;
        api.sendReq(req);

    }

    /**
     * Bitmap转成字节数组
     *
     * @param bitmap
     * @return
     */
    private static byte[] bmpToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        bitmap.recycle();//自由选择是否进行回收
        byte[] result = output.toByteArray();//转换成功了
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
