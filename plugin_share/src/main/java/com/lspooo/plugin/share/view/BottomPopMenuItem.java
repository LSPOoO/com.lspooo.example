package com.lspooo.plugin.share.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.NavigationView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lspooo.plugin.share.R;


/**
 * Created by lspooo on 2018/4/28.
 */

public class BottomPopMenuItem extends LinearLayout{

    private Context context;
    private ImageView menuIcon;
    private TextView menuTitle;

    public BottomPopMenuItem(Context context) {
        this(context, null);
    }

    public BottomPopMenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomPopMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_botton_pop_menu_item, this, true);
        menuIcon = (ImageView) findViewById(R.id.menu_icon_iv);
        menuTitle = (TextView) findViewById(R.id.menu_title_tv);

        if (attrs != null){
            TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.bottom_pop_menu);
            menuIcon.setImageDrawable(localTypedArray.getDrawable(R.styleable.bottom_pop_menu_icon));
            menuTitle.setText(localTypedArray.getString(R.styleable.bottom_pop_menu_name));
            localTypedArray.recycle();
        }
    }

    public void setMenuIcon(int resId) {
        if (resId < 0) {
            return;
        }
        menuIcon.setImageResource(resId);
    }

    public void setMenuTitle(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        menuTitle.setText(text);
    }

    public void setClickListener(final OnClickListener onClickListener){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(view);
                }
            }
        });
    }
}
