package com.lspooo.plugin.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lspooo.plugin.common.R;

/**
 * Created by lspooo on 2018/2/6.
 */

public class CCPEditText extends LinearLayout {

    private Context context;
    private ImageView leftIcon;
    private ImageView rightIcon;
    private EditText editText;
    private View editLine;

    private OnClickListener leftIconClickListener;
    private OnClickListener rightIconClickListener;

    public CCPEditText(Context context) {
        this(context, null);
    }

    public CCPEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CCPEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.layout_ccp_edit_text, this, true);

        leftIcon = (ImageView) findViewById(R.id.leftIcon);
        rightIcon = (ImageView) findViewById(R.id.rightIcon);
        editText = (EditText) findViewById(R.id.editText);
        editLine = findViewById(R.id.edit_line);
        if (attrs != null) {
            TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.edit_text_view);
            //左部图片
            Drawable leftIconDrawable = localTypedArray.getDrawable(R.styleable.edit_text_view_leftIcon);
            if (leftIconDrawable != null) {
                leftIcon.setImageDrawable(leftIconDrawable);
                leftIcon.setVisibility(VISIBLE);
            }
            //右部图片
            Drawable rightIconDrawable = localTypedArray.getDrawable(R.styleable.edit_text_view_rightIcon);
            if (rightIconDrawable != null) {
                rightIcon.setImageDrawable(rightIconDrawable);
                rightIcon.setVisibility(VISIBLE);
            }
            localTypedArray.recycle();
        }

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                editLine.setBackgroundResource(focus ? R.color.base_color : R.color.list_divider);
            }
        });
        leftIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftIconClickListener != null) {
                    leftIconClickListener.onClick(view);
                }
            }
        });
        rightIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightIconClickListener != null) {
                    rightIconClickListener.onClick(view);
                }
            }
        });
    }

    public void setLeftIcon(int resId){
        leftIcon.setImageResource(resId);
    }

    public void setLeftIconClickListener(OnClickListener leftIconClickListener) {
        this.leftIconClickListener = leftIconClickListener;
    }

    public void setRightIconClickListener(OnClickListener rightIconClickListener) {
        this.rightIconClickListener = rightIconClickListener;
    }
}
