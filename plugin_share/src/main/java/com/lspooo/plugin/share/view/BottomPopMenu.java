package com.lspooo.plugin.share.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.lspooo.plugin.share.R;

import java.util.List;

/**
 * Created by lspooo on 2018/4/28.
 */

public class BottomPopMenu extends HorizontalScrollView{

    private Context context;
    private LinearLayout container;

    public BottomPopMenu(Context context) {
        this(context, null);
    }

    public BottomPopMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomPopMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_botton_pop_menu, this, true);
        container = (LinearLayout) findViewById(R.id.container);
    }

    public void init(List<MenuItem> menuItemList){
        for (MenuItem menuItem : menuItemList) {
            BottomPopMenuItem bottomPopMenuItem = new BottomPopMenuItem(context);
            bottomPopMenuItem.setMenuIcon(menuItem.getMenuIconResId());
            bottomPopMenuItem.setMenuTitle(menuItem.getMenuTitle());
            bottomPopMenuItem.setClickListener(menuItem.getOnClickListener());
            container.addView(bottomPopMenuItem);
        }
    }

    public static class MenuItem{

        private int menuIconResId;
        private String menuTitle;
        private OnClickListener onClickListener;

        public MenuItem(int menuIconResId, String menuTitle, OnClickListener onClickListener) {
            this.menuIconResId = menuIconResId;
            this.menuTitle = menuTitle;
            this.onClickListener = onClickListener;
        }

        public int getMenuIconResId() {
            return menuIconResId;
        }

        public String getMenuTitle() {
            return menuTitle;
        }

        public OnClickListener getOnClickListener() {
            return onClickListener;
        }
    }
}
