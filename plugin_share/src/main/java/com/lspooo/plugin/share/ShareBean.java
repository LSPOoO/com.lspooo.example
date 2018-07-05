package com.lspooo.plugin.share;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lspooo on 2018/7/5.
 */

public class ShareBean{

    private Type type;
    private String content;
    private Bitmap bitmap;

    public ShareBean(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public ShareBean(Type type, Bitmap bitmap) {
        this.type = type;
        this.bitmap = bitmap;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static ShareBean createTXTShareBean(String content){
        return new ShareBean(Type.TXT, content);
    }

    public static ShareBean createImageShareBean(Bitmap bitmap){
        return new ShareBean(Type.IMAGE, bitmap);
    }

    public enum Type{
        TXT,
        IMAGE,
        PAGE,
        VIDEO
    }


}
