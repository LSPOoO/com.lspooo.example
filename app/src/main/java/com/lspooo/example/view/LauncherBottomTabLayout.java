package com.lspooo.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lspooo.example.R;
import com.lspooo.example.plugin.common.tools.BackwardSupportUtil;
import com.lspooo.example.plugin.common.tools.ResourceHelper;
import com.lspooo.example.plugin.common.ui.CommonActivity;

public class LauncherBottomTabLayout extends LinearLayout {

    private Context context;
    private LauncherBottomTabView communicationTab;
    private LauncherBottomTabView contactTab;
    private LauncherBottomTabView findTab;
    private LauncherBottomTabView settingTab;

    private OnTabSelectedListener tabSelectedListener;

    public LauncherBottomTabLayout(Context context) {
        this(context, null);
    }

    public LauncherBottomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LauncherBottomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init(){
        LayoutInflater.from(context).inflate(R.layout.tab_layout, this, true);

        communicationTab = (LauncherBottomTabView) findViewById(R.id.tab_communication);
        communicationTab.setTag(LauncherBottomTabView.TAB_COMMUNICATION);
        communicationTab.setOnClickListener(tabClickListener);

        contactTab = (LauncherBottomTabView) findViewById(R.id.tab_contact);
        contactTab.setTag(LauncherBottomTabView.TAB_CONTACT);
        contactTab.setOnClickListener(tabClickListener);

        findTab = (LauncherBottomTabView) findViewById(R.id.tab_find);
        findTab.setTag(LauncherBottomTabView.TAB_FIND);
        findTab.setOnClickListener(tabClickListener);

        settingTab = (LauncherBottomTabView) findViewById(R.id.tab_setting);
        settingTab.setTag(LauncherBottomTabView.TAB_SETTING);
        settingTab.setOnClickListener(tabClickListener);
    }

    private OnClickListener tabClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (Integer) v.getTag();
            tabSelected(index);
        }
    };

    public void setTabSelected(int tabIndex){
        tabSelected(tabIndex);
    }

    private void tabSelected(int tabIndex){
        if (tabSelectedListener != null){
            tabSelectedListener.onTabSelected(tabIndex);
        }
        switch (tabIndex){
            case 0:
                communicationTab.setChecked(true);
                contactTab.setChecked(false);
                findTab.setChecked(false);
                settingTab.setChecked(false);
                break;
            case 1:
                communicationTab.setChecked(false);
                contactTab.setChecked(true);
                findTab.setChecked(false);
                settingTab.setChecked(false);
                break;
            case 2:
                communicationTab.setChecked(false);
                contactTab.setChecked(false);
                findTab.setChecked(true);
                settingTab.setChecked(false);
                break;
            case 3:
                communicationTab.setChecked(false);
                contactTab.setChecked(false);
                findTab.setChecked(false);
                settingTab.setChecked(true);
                break;
            default:
                break;
        }
    }

    public void setTabSelectedListener(OnTabSelectedListener tabSelectedListener) {
        this.tabSelectedListener = tabSelectedListener;
    }

    public interface OnTabSelectedListener{
        void onTabSelected(int tabIndex);
    }


}
