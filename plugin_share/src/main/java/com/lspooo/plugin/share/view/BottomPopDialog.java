package com.lspooo.plugin.share.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lspooo.plugin.share.R;
import com.lspooo.plugin.share.ShareBean;
import com.lspooo.plugin.share.WXShareUtils;
import com.tencent.mm.sdk.openapi.SendMessageToWX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lspooo on 2018/4/28.
 */

public class BottomPopDialog extends Dialog{

    private Context mContext;
    private View container;
    private BottomPopMenu bottomPopMenu;
    private ShareBean shareBean;

    public BottomPopDialog(Context context, ShareBean bean) {
        super(context, R.style.BottomAnimDialogStyle);
        this.mContext = context;
        this.shareBean = bean;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        container = inflater.inflate(R.layout.layout_bottom_dialog, null);
        Window window = this.getWindow();
        if (window != null) {
            //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.BOTTOM);
        }
        setContentView(container);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initBottomPopMenu();
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initBottomPopMenu() {
        bottomPopMenu = (BottomPopMenu) container.findViewById(R.id.bottom_pop_menu);
        List<BottomPopMenu.MenuItem> menuItemList = new ArrayList<>();
        menuItemList.add(new BottomPopMenu.MenuItem(R.drawable.ic_shopping, "微信好友", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXShareUtils.shareToWXSceneSession(mContext, shareBean);
                dismiss();
            }
        }));

        menuItemList.add(new BottomPopMenu.MenuItem(R.drawable.ic_shopping, "朋友圈", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXShareUtils.shareToWXSceneTimeline(mContext, shareBean);
                dismiss();
            }
        }));
        bottomPopMenu.init(menuItemList);
    }
}
