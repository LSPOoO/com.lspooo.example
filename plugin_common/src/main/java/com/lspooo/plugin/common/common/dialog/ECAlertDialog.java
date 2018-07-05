package com.lspooo.plugin.common.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lspooo.plugin.common.R;

/**
 * Created by lspooo on 2018/1/16.
 */

public class ECAlertDialog extends Dialog{

    private Context context;

    private View mRootView;
    private View mTitleLayout;
    private TextView mTitleTv;
    private View mMessageLayout;
    private TextView mMessageTv;
    private Button mPositiveBtn;
    private Button mNegativeBtn;
    /**
     * 对话框是否可以取消
     */
    private boolean mCancelable;


    public ECAlertDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        this.context = context;
        mRootView = View.inflate(context, R.layout.layout_alert_dialog, null);
        mTitleLayout = mRootView.findViewById(R.id.dialog_title_layout);
        mTitleTv = (TextView) mRootView.findViewById(R.id.dialog_title_tv);
        mMessageLayout = mRootView.findViewById(R.id.dialog_content_layout);
        mMessageTv = (TextView) mRootView.findViewById(R.id.dialog_message_tv);
        mPositiveBtn = (Button) mRootView.findViewById(R.id.positive_btn);
        mNegativeBtn = (Button) mRootView.findViewById(R.id.negative_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mRootView);
    }


    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        mCancelable = flag;
        setCanceledOnTouchOutside(mCancelable);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
        }
    }


    public void setAlertParams(final Builder.AlertParams params) {
        // 设置对话框标题
        if (params.title != null && params.title.length() > 0) {
            this.setTitle(params.title);
        }
        // 设置标题颜色
        if (params.mTitleColor != 0) {
            this.mTitleTv.setTextColor(ColorStateList.valueOf(params.mTitleColor));
        }

        // 设置标题支持最大行数
        if (params.mTitleMaxLines != 0) {
            this.mTitleTv.setMaxLines(params.mTitleMaxLines);
        }

        // 设置消息内容
        if (params.message != null && params.message.length() > 0) {
            this.setMessage(params.message);
        }

        if (params.positive != null && params.positive.length() > 0) {
            this.setPositiveButton(params.positive, params.onPositiveListener, params.okDismiss);
            if (params.mColor != -1) {
                setPositiveColor(params.mColor);
            }
        }

        if (params.negative != null && params.negative.length() > 0) {
            this.setNegativeButton(params.negative, params.onNegativeListener, true);
        }

        if (params.onCancelListener != null) {
            this.setOnCancelListener(params.onCancelListener);
        }

        if (params.onDismissListener != null) {
            this.setOnDismissListener(params.onDismissListener);
        }

        this.setCancelable(params.cancelable);
        this.mCancelable = params.cancelable;
        if (!this.mCancelable) {
            super.setCancelable(params.onCancelBackKey);
        }
    }


    /**
     * 设置对话框显示标题
     *
     * @param title 标题资源文字
     */
    public void setTitle(CharSequence title) {
        mTitleLayout.setVisibility(View.VISIBLE);
        mTitleTv.setVisibility(View.VISIBLE);
        mTitleTv.setMaxLines(2);
        mTitleTv.setText(title);
    }

    /**
     * 设置对话框显示标题
     *
     * @param titleId 标题资源
     */
    public void setTitle(int titleId) {
        mTitleLayout.setVisibility(View.VISIBLE);
        mTitleTv.setVisibility(View.VISIBLE);
        mTitleTv.setMaxLines(2);
        mTitleTv.setText(titleId);
    }


    /**
     * 设置对话框显示消息内容
     *
     * @param message 消息内容
     */
    public void setMessage(CharSequence message) {
        mMessageLayout.setVisibility(View.VISIBLE);
        mMessageTv.setVisibility(View.VISIBLE);
        mMessageTv.setText(message);
    }


    /**
     * 设置确定按钮属性
     *
     * @param positive           显示文字
     * @param onPositiveListener 响应事件回调接口
     * @param dismiss            是否关闭
     */
    public void setPositiveButton(CharSequence positive, final OnClickListener onPositiveListener, final boolean dismiss) {
        if (mPositiveBtn != null) {
            mPositiveBtn.setVisibility(View.VISIBLE);
            mPositiveBtn.setText(positive);
            mPositiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPositiveListener != null) {
                        onPositiveListener.onClick(ECAlertDialog.this, DialogInterface.BUTTON_POSITIVE);
                    }

                    if (dismiss) {
                        ECAlertDialog.this.dismiss();
                    }
                }
            });
        }
    }


    public final void setPositiveColor(int color) {
        this.mPositiveBtn.setTextColor(color);
    }

    /**
     * 设置取消按钮属性
     *
     * @param negative           显示文字
     * @param onNegativeListener 响应事件回调接口
     */
    public void setNegativeButton(CharSequence negative, final OnClickListener onNegativeListener, final boolean cancel) {
        if (mNegativeBtn != null) {
            mNegativeBtn.setVisibility(View.VISIBLE);
            mNegativeBtn.setText(negative);
            mNegativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNegativeListener != null) {
                        onNegativeListener.onClick(ECAlertDialog.this, DialogInterface.BUTTON_POSITIVE);
                    }

                    if (cancel) {
                        ECAlertDialog.this.cancel();
                    }
                }
            });
        }
    }


    /**
     * 设置对话框对应的按钮是否可用
     *
     * @param which   按钮所对应的标示
     * @param enabled 是否可用
     * @see DialogInterface#BUTTON_POSITIVE
     * @see DialogInterface#BUTTON_NEGATIVE
     */
    public final void showButton(int which, boolean enabled) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (enabled) {
                    mPositiveBtn.setVisibility(View.VISIBLE);
                    if (mNegativeBtn.getVisibility() == View.VISIBLE) {
                        return;
                    }
                    return;
                }
                mPositiveBtn.setVisibility(View.GONE);

                break;
            case DialogInterface.BUTTON_NEGATIVE:
                if (enabled) {
                    mNegativeBtn.setVisibility(View.VISIBLE);
                    if (mPositiveBtn.getVisibility() == View.VISIBLE) {
                        return;
                    }
                    return;
                }
                mNegativeBtn.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }



    public static class Builder {

        /**
         * 对话框构建参数信息
         */
        public AlertParams mAlertParams;
        /**
         * 上下文
         */
        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
            mAlertParams = new AlertParams();
        }

        /**
         * 根据设置的对话框参数信息生成对话框
         *
         * @return 对话框
         */
        public ECAlertDialog create() {
            ECAlertDialog dialog = new ECAlertDialog(mContext);
            dialog.setAlertParams(mAlertParams);
            return dialog;
        }

        /**
         * 设置确定按钮响应是否关闭对话框
         *
         * @param okDismiss 是否关闭对话框
         * @return Builder
         */
        public Builder setOkClickDismiss(boolean okDismiss) {
            mAlertParams.okDismiss = okDismiss;
            return this;
        }

        /**
         * 设置是否可用取消对话框
         *
         * @return Builder
         */
        public Builder setSuperCancle() {
            mAlertParams.superCancelable = true;
            return this;
        }

        /**
         * 设置对话框确定按钮文本
         * @param text 确定按钮文本
         * @return Builder
         */
        public Builder setPositiveText(int text) {
            mAlertParams.positive = mContext.getString(text);
            return this;
        }

        /**
         * 设置对话框确定按钮文本
         *
         * @param text 确定按钮文本
         * @return Builder
         */
        public Builder setPositiveText(String text) {
            mAlertParams.positive = text;
            return this;
        }

        /**
         * 设置对话框确定按钮回调事件
         *
         * @param l 按钮回调事件
         * @return Builder
         */
        public Builder setOnPositiveClickListener(DialogInterface.OnClickListener l) {
            mAlertParams.onPositiveListener = l;
            return this;
        }

        /**
         * 设置确认按钮的颜色
         *
         * @param color 颜色
         * @return Builder
         */
        public Builder setPositiveColor(int color) {
            mAlertParams.mColor = color;
            return this;
        }

        /**
         * 设置确定按钮资源已经响应事件回调接口
         *
         * @param resId 确定按钮资源
         * @param l     响应事件回调接口
         * @return Builder
         */
        public Builder setPositiveButton(int resId, DialogInterface.OnClickListener l) {
            setPositiveButton(mContext.getString(resId), l);
            return this;
        }

        /**
         * 设置确定按钮资源已经响应事件回调接口
         *
         * @param text 确定按钮显示内容
         * @param l    响应事件回调接口
         * @return Builder
         */
        public Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener l) {
            mAlertParams.positive = text;
            mAlertParams.onPositiveListener = l;
            return this;
        }

        /**
         * 设置确定按钮资源已经响应事件回调接口
         *
         * @param dismiss 是否关闭
         * @param l       响应事件回调接口
         * @return Builder
         */
        public Builder setPositiveButton(boolean dismiss, DialogInterface.OnClickListener l) {
            mAlertParams.onPositiveListener = l;
            mAlertParams.okDismiss = dismiss;
            return this;
        }

        /**
         * 设置对话框取消按钮文本
         *
         * @param text 文本
         * @return Builder
         */
        public Builder setNegativeText(int text) {
            mAlertParams.negative = mContext.getString(text);
            return this;
        }

        /**
         * 设置对话框取消按钮文本
         * @param text 文本
         * @return Builder
         */
        public Builder setNegativeText(String text) {
            mAlertParams.negative = text;
            return this;
        }

        /**
         * 设置对话框取消按钮回调事件
         *
         * @param l 按钮回调事件
         * @return Builder
         */
        public Builder setOnNegativeClickListener(DialogInterface.OnClickListener l) {
            mAlertParams.onNegativeListener = l;
            return this;
        }

        /**
         * 设置对话框取消按钮显示内容已经相应的响应事件回调接口
         *
         * @param resId 对话框取消按钮显示内容资源
         * @param l     相应的响应事件回调接口
         * @return Builder
         */
        public Builder setNegativeButton(int resId, DialogInterface.OnClickListener l) {
            return setNegativeButton(mContext.getString(resId), l);

        }

        /**
         * 设置对话框取消按钮显示内容已经相应的响应事件回调接口
         *
         * @param text 对话框取消按钮显示内容
         * @param l    相应的响应事件回调接口
         * @return Builder
         */
        public Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener l) {
            mAlertParams.negative = text;
            mAlertParams.onNegativeListener = l;
            return this;
        }

        /**
         * 设置对话框显示的标题内容
         *
         * @param resId 对话框显示的标题内容资源
         * @return Builder
         */
        public Builder setTitle(int resId) {
            return setTitle(mContext.getString(resId));
        }

        /**
         * 设置对话框显示的标题内容
         *
         * @param text 对话框显示的标题内容
         * @return Builder
         */
        public Builder setTitle(CharSequence text) {
            mAlertParams.title = text;
            return this;
        }

        /**
         * 设置对话框显示的内容
         *
         * @param resId 对话框显示的内容资源
         * @return Builder
         */
        public Builder setMessage(int resId) {
            return setMessage(mContext.getString(resId));
        }

        /**
         * 设置对话框显示的内容
         *
         * @param text 对话框显示的内容
         * @return Builder
         */
        public Builder setMessage(CharSequence text) {
            mAlertParams.message = text;
            return this;
        }

        /**
         * 设置对话框取消显示响应事件回调接口
         *
         * @param l 对话框取消显示响应事件回调接口
         * @return Builder
         */
        public Builder setOnCancelListener(DialogInterface.OnCancelListener l) {
            mAlertParams.onCancelListener = l;
            return this;
        }

        /**
         * 设置对话框销毁效应事件回调接口
         *
         * @param l 对话框销毁效应事件回调接口
         * @return Builder
         */
        public Builder setOnDismissListener(DialogInterface.OnDismissListener l) {
            mAlertParams.onDismissListener = l;
            return this;
        }

        /**
         * 点击返回按钮或者触摸对话框之外区域是否关闭
         *
         * @param flag 是否可用取消
         * @return Builder
         */
        public Builder setCancelable(boolean flag) {
            mAlertParams.cancelable = flag;
            return this;
        }

        /**
         * 设置仅限于点击返回按键才可以取消／关闭对话框
         * 如果调用了{@link #setCancelable(boolean)} 且设置为true ，那么这个方法设置无效
         *
         * @param onCancelBackKey 仅限于返回按键关闭
         * @return Builder
         */
        public Builder setOnCancelBackKey(boolean onCancelBackKey) {
            mAlertParams.onCancelBackKey = onCancelBackKey;
            return this;
        }

        /**
         * 设置对话框来源区域显示位置
         *
         * @param gravity 来源区域显示位置
         * @return Builder
         */
        public Builder setSourceGravity(int gravity) {
            mAlertParams.mSourceGravity = gravity;
            return this;
        }

        /**
         * 是否需要有边距
         *
         * @param isHasPadding
         * @return Builder
         */
        public Builder setIsHasPadding(boolean isHasPadding) {
            mAlertParams.isHasPadding = isHasPadding;
            return this;
        }


        public static class AlertParams{

            /**
             * 对话框显示的标题
             */
            CharSequence title;
            /**
             * 对话框显示的文本内容
             */
            CharSequence message;
            /**
             * 确定按钮显示内容
             */
            CharSequence positive;
            /**
             * 关闭按钮显示内容
             */
            CharSequence negative;
            /**
             * 确定按钮默认颜色
             */
            int mColor = -1;
            /**
             * 对话框是否可用取消
             */
            boolean cancelable = true;
            /**
             * 确定按钮响应是否关闭对话框
             */
            boolean okDismiss = true;
            boolean superCancelable = false;
            /**
             * 返回按键是否可用取消
             * */
            boolean onCancelBackKey = false;
            /**
             * 对话框确定按钮响应事件回调接口
             */
            OnClickListener onPositiveListener;
            /**
             * 对话框关闭按钮响应事件回调接口
             */
            OnClickListener onNegativeListener;
            /**
             * 对话框取消显示按钮响应事件回调接口
             */
            OnCancelListener onCancelListener;
            /**
             * 对话框关闭响应事件回调接口
             */
            OnDismissListener onDismissListener;
            /**
             * 是否需要边距
             */
            public boolean isHasPadding = true;
            /**
             * 引用来源显示位置
             */
            int mSourceGravity = Gravity.LEFT;
            /**
             * 标题颜色
             */
            public int mTitleColor = 0;
            /**
             * 标题最大行数
             */
            public int mTitleMaxLines = 0;
        }
    }
}
