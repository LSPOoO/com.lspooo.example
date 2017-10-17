package com.lspooo.plugin.common.common.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lspooo.plugin.common.R;


public class RXListDialog extends AlertDialog {

    /** 上下文 */
    public Context mContext;
    /** 列表对话框选项列表 */
    private ListView mListView;
    /** 列表对话框布局 */
    private View mContentView;
    /** 列表对话框标题分割线 */
    private View mTitleDivider;
    /** 列表对话框标题 */
    private TextView mTitle;
    /** 列表对话框选项适配器 */
    private BaseAdapter mBaseAdapter;
    /** 列表对话框选项事件回调接口 */
    private AdapterView.OnItemClickListener mOnItemClickListener;
    /** 列表对话框标题内容 */
    private CharSequence mDialogTitle;

    /**
     * 构造方法
     * @param context 上下文
     */
    public RXListDialog(Context context) {
        super(context, R.style.AlertDialogStyle);

        mContentView = View.inflate(context, R.layout.layout_list_dialog, null);
        mTitleDivider = mContentView.findViewById(R.id.title_divider);
        mTitle = (TextView) mContentView.findViewById(R.id.title);
        mListView = (ListView) mContentView.findViewById(R.id.list);

        setCanceledOnTouchOutside(true);
    }

    /**
     * 列表对话框自定义适配器
     * @param adapter 适配器
     */
    public void setAdapter(BaseAdapter adapter) {
        mBaseAdapter = adapter;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mContentView);
    }

    /**
     * 设置列表对话框选项事件触发回调接口
     * @param l 选项事件触发回调接口
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    /**
     * 设置列表对话框标题
     * @param text 文本
     */
    public void setTitle(CharSequence text) {
        if(text != null) {
            mDialogTitle = text;
            return;
        }

        mDialogTitle = null;
    }

    /**
     * 现实对话框
     */
    @Override
    public void show() {
        if(TextUtils.isEmpty(mDialogTitle)) {
            mTitleDivider.setVisibility(View.GONE);
            mTitle.setVisibility(View.GONE);
        } else {
            mTitleDivider.setVisibility(View.VISIBLE);
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(mDialogTitle);
        }

        if(mOnItemClickListener != null) {
            mListView.setOnItemClickListener(mOnItemClickListener);
        }

        if(mBaseAdapter != null) {
            mListView.setAdapter(mBaseAdapter);
        }
        super.show();
    }
}
