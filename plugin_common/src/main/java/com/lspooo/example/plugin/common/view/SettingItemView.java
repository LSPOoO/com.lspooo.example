package com.lspooo.example.plugin.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lspooo.example.plugin.common.R;

/**
 * Created by LSP on 2017/9/23.
 */

public class SettingItemView extends LinearLayout{

    public static final int ACCESSORY_TYPE_NONE = 0;
    public static final int ACCESSORY_TYPE_ARROW = 1;
    public static final int ACCESSORY_TYPE_SWITCH = 2;
    public static final int ACCESSORY_TYPE_RADIO = 3;

    private Context context;
    private ImageView settingIcon;
    private TextView settingTitleView;
    private TextView settingSummaryView;
    private View settingDividerView;
    private CheckedTextView checkedTextView;
    private SwitchButton settingSwitchButton;

    private int mAccessoryType;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.layout_setting_item, this, true);

        settingIcon = (ImageView) findViewById(R.id.setting_icon);
        settingTitleView = (TextView) findViewById(R.id.setting_title);
        settingSummaryView = (TextView) findViewById(R.id.setting_summary);
        settingDividerView = findViewById(R.id.setting_divider);
        checkedTextView = (CheckedTextView) findViewById(R.id.check_view);
        settingSwitchButton = (SwitchButton) findViewById(R.id.setting_switch_button);

        if (attrs != null){
            TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.setting_item_view);
            setSettingIcon(localTypedArray.getDrawable(R.styleable.setting_item_view_icon));
            setSettingTitle(localTypedArray.getString(R.styleable.setting_item_view_title));
            setSettingSummary(localTypedArray.getString(R.styleable.setting_item_view_summary));
            setAccessoryType(localTypedArray.getInt(R.styleable.setting_item_view_accessoryType , 0));
            setDividerVisibility(localTypedArray.getBoolean(R.styleable.setting_item_view_showDivider , true));
            localTypedArray.recycle();
        }
    }

    /**
     * 设置图标
     * @param drawable
     */
    public void setSettingIcon(Drawable drawable){
        if(settingIcon == null || drawable == null){
            return;
        }
        settingIcon.setVisibility(VISIBLE);
        settingIcon.setImageDrawable(drawable);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setSettingTitle(String title){
        if (settingTitleView == null){
            return;
        }
        settingTitleView.setText(TextUtils.isEmpty(title) ? "" : title);
    }

    /**
     * 设置标题
     * @param titleResId
     */
    public void setSettingTitle(int titleResId){
        String text = context.getString(titleResId);
        setSettingTitle(text);
    }

    /**
     * 设置描述
     * @param summary
     */
    public void setSettingSummary(String summary) {
        if (settingSummaryView == null){
            return;
        }
        if (TextUtils.isEmpty(summary)){
            settingSummaryView.setText("");
            settingSummaryView.setVisibility(GONE);
        } else{
            settingSummaryView.setText(summary);
            settingSummaryView.setVisibility(GONE);
        }
    }

    /**
     * 设置描述
     * @param summaryResId
     */
    public void setSettingSummary(int summaryResId) {
        String text = context.getString(summaryResId);
        setSettingSummary(text);
    }

    /**
     * 设置类型
     * @param type
     */
    public void setAccessoryType(int type) {
        switch (type){
            case 0:
                mAccessoryType = ACCESSORY_TYPE_NONE;
                checkedTextView.setVisibility(VISIBLE);
                settingSwitchButton.setVisibility(GONE);
                break;
            case 1:
                mAccessoryType = ACCESSORY_TYPE_ARROW;
                checkedTextView.setVisibility(VISIBLE);
                settingSwitchButton.setVisibility(GONE);
                checkedTextView.setCheckMarkDrawable(R.drawable.ic_setting_enter);
                checkedTextView.setPadding(0, 0, 24, 0);
                checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                break;
            case 2:
                mAccessoryType = ACCESSORY_TYPE_SWITCH;
                checkedTextView.setVisibility(GONE);
                settingSwitchButton.setVisibility(VISIBLE);
                break;
            case 3:
                mAccessoryType = ACCESSORY_TYPE_RADIO;
                checkedTextView.setVisibility(VISIBLE);
                settingSwitchButton.setVisibility(GONE);
                checkedTextView.setCheckMarkDrawable(R.drawable.choose_icon);
                checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                break;
            default:
                break;
        }
    }

    /**
     * 底部分割线是否显示
     * @param visibility
     */
    public void setDividerVisibility(boolean visibility){
        if (settingDividerView == null){
            return;
        }
        settingDividerView.setVisibility(visibility ? VISIBLE : GONE);
    }

    /**
     * 是否处于选中状态
     * @return
     */
    public boolean isChecked() {
        if(mAccessoryType == ACCESSORY_TYPE_RADIO) {
            if (checkedTextView == null){
                return false;
            }
            return checkedTextView.isChecked();
        }
        if (mAccessoryType == ACCESSORY_TYPE_SWITCH){
            if (settingSwitchButton == null){
                return false;
            }
            return settingSwitchButton.isChecked();
        }
        return false;
    }

    /**
     * 设置状态
     * @param checked
     */
    public void setChecked(boolean checked) {
        if(mAccessoryType == ACCESSORY_TYPE_RADIO) {
            if (checkedTextView == null){
                return;
            }
            checkedTextView.setChecked(checked);
        } else if (mAccessoryType == ACCESSORY_TYPE_SWITCH){
            if (settingSwitchButton == null){
                return;
            }
            settingSwitchButton.setChecked(checked);
        }
    }

    /**
     * 切换选中状态
     */
    public void toggle() {
        if(mAccessoryType == ACCESSORY_TYPE_RADIO ) {
            if (checkedTextView == null){
                return;
            }
            checkedTextView.toggle();
        } else if (mAccessoryType == ACCESSORY_TYPE_SWITCH){
            if (settingSwitchButton == null){
                return;
            }
            settingSwitchButton.toggle();
        }
    }
}
