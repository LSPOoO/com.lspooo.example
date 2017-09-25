package com.lspooo.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lspooo.example.R;

/**
 * Created by LSP on 2017/9/24.
 */

public class LauncherBottomTabView extends RelativeLayout{

    public final static int TAB_COMMUNICATION = 0;
    public final static int TAB_CONTACT = 1;
    public final static int TAB_FIND = 2;
    public final static int TAB_SETTING = 3;

    private Context context;
    private ImageView icon;
    private TextView titleTv;
    private TextView unReadCountTv;
    private ImageView unReadDot;

    private boolean checked = false;
    private Drawable iconNormal;
    private Drawable iconOn;

    public LauncherBottomTabView(Context context) {
        this(context, null);
    }

    public LauncherBottomTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LauncherBottomTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.single_tab_view, this, true);
        icon = (ImageView) findViewById(R.id.tab_view_iv);
        unReadCountTv = (TextView) findViewById(R.id.unread_count);
        unReadDot = (ImageView) findViewById(R.id.unread_dot);
        titleTv = (TextView) findViewById(R.id.tab_text_tv);

        if (attrs != null){
            TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.launcher_tab_view);
            iconNormal = localTypedArray.getDrawable(R.styleable.launcher_tab_view_tabIconNormal);
            iconOn = localTypedArray.getDrawable(R.styleable.launcher_tab_view_tabIconOn);
            String unReadCount = localTypedArray.getString(R.styleable.launcher_tab_view_tabUnReadCount);
            checked = localTypedArray.getBoolean(R.styleable.launcher_tab_view_tabChecked, false);
            boolean unReadCountVisibility = localTypedArray.getBoolean(R.styleable.launcher_tab_view_tabUnReadCountVisibility, false);
            boolean unReadDotVisibility = localTypedArray.getBoolean(R.styleable.launcher_tab_view_tabUnReadDotVisibility, false);
            setUnReadCount(unReadCount, unReadCountVisibility);
            setUnReadDotVisibility(unReadDotVisibility);
            setChecked(checked);
            setTabTitle(localTypedArray.getString(R.styleable.launcher_tab_view_tabTitle));
            localTypedArray.recycle();
        }
    }

    private void setTabIcon(){
        if (icon == null || iconOn == null || iconNormal == null){
            return;
        }
        icon.setImageDrawable(checked ? iconOn : iconNormal);
    }

    public void setTabTitle(String title){
        if (titleTv == null){
            return;
        }
        titleTv.setText(TextUtils.isEmpty(title) ? "" : title);
    }

    public void setUnReadCount(String unReadCount, boolean unReadCountVisibility){
        if (unReadCountTv == null){
            return;
        }
        if (TextUtils.isEmpty(unReadCount) || !unReadCountVisibility){
            unReadCountTv.setVisibility(GONE);
        } else{
            unReadCountTv.setVisibility(VISIBLE);
            unReadCountTv.setText(unReadCount);
        }
    }

    public void setUnReadDotVisibility(boolean unReadDotVisibility){
        if (unReadDot == null){
            return;
        }
        unReadDot.setVisibility(unReadDotVisibility ? VISIBLE : GONE);
    }

    public void setChecked(boolean isChecked){
        checked = isChecked;
        setTabIcon();
    }
}
