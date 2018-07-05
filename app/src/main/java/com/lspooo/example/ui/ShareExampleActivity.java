package com.lspooo.example.ui;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import com.lspooo.example.R;
import com.lspooo.plugin.share.ShareBean;
import com.lspooo.plugin.share.view.BottomPopDialog;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.CommonActivity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lspooo on 2018/2/6.
 */

public class ShareExampleActivity extends CommonActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(getString(R.string.menu_launcher_action_share_example));

        findViewById(R.id.share_txt_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareBean bean = ShareBean.createTXTShareBean("微信小动作越来越多，但太多功能已成“鸡肋”");
                BottomPopDialog dialog = new BottomPopDialog(ShareExampleActivity.this, bean);
                dialog.show();
            }
        });
        findViewById(R.id.share_image_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareBean bean = ShareBean.createImageShareBean(getImageFromAssetsFile("WechatIMG39.jpeg"));
                BottomPopDialog dialog = new BottomPopDialog(ShareExampleActivity.this, bean);
                dialog.show();
            }
        });
    }

    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_share_example;
    }
}
