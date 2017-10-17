package com.lspooo.plugin.common.common.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lspooo.plugin.common.R;
import com.lspooo.plugin.common.common.dialog.RXListDialog;


/**
 * 自定义的列表风格的菜单
 *
 * @author 容联•云通讯
 * @since 2017/3/23
 */
public class RXContentMenuHelper implements AdapterView.OnItemClickListener {

    private static final String TAG = "RongXin.RXContentMenuHelper";

    /** 上下文 */
    private Context mContext;
    /** 布局加载工具 */
    private LayoutInflater mLayoutInflater;
    /** 自定义的列表对话框 */
    private RXListDialog mListDialog;
    /** 菜单子项 */
    private ActionMenu mActionMenu;
    /** 菜单创建回调接口 */
    private ActionMenu.OnCreateActionMenuListener mOnCreateActionMenuListener;
    /** 菜单绑定数据回调接口-标题 */
    private ActionMenu.OnBuildActionMenuIconListener mOnBuildActionMenuIconListener;
    /** 菜单绑定数据回调接口-文本 */
    private ActionMenu.OnBuildActionMenuTextListener mOnBuildActionMenuTextListener;
    /** 菜单选择响应回调接口 */
    private ActionMenu.OnActionMenuItemSelectedListener mOnActionMenuItemSelectedListener;
    /** 菜单内容适配器 */
    private ContentMenuAdapter mMenuAdapter;

    /**
     * 针对于ListView列表定义的长按响应事件处理
     */
    final private class AdapterViewOnItemLongClickListener implements AdapterView.OnItemLongClickListener {

        final private View.OnCreateContextMenuListener mContextMenuListener;

        AdapterViewOnItemLongClickListener(View.OnCreateContextMenuListener l) {
            mContextMenuListener = l;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            mActionMenu.clear();
            AdapterView.AdapterContextMenuInfo contextMenuInfo = new AdapterView.AdapterContextMenuInfo(view, position, id);
            mContextMenuListener.onCreateContextMenu(mActionMenu, view, contextMenuInfo);
            for(RXActionMenuItem actionMenu : mActionMenu.getMenuItems()) {
                actionMenu.setContextMenuInfo(contextMenuInfo);
            }
            show();
            return true;
        }

    }

    /**
     * 针对普通的View注册的长按响应事件处理
     */
    final private class ViewOnLongClickListener implements View.OnLongClickListener {

        final private View.OnCreateContextMenuListener mContextMenuListener;

        ViewOnLongClickListener(View.OnCreateContextMenuListener l) {
            mContextMenuListener = l;
        }
        @Override
        public boolean onLongClick(View v) {
            mActionMenu.clear();
            mContextMenuListener.onCreateContextMenu(mActionMenu, v, null);
            show();
            return false;
        }

    }

    /**
     * 构造方法
     * @param context 上下文
     */
    public RXContentMenuHelper(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mListDialog = new RXListDialog(context);
        mActionMenu = new ActionMenu();
    }

    /**
     * 关闭
     */
    public void dismiss() {
        if (mListDialog == null){
            return;
        }
        if(!mListDialog.isShowing()) {
            return;
        }
        mListDialog.dismiss();
    }

    public boolean isShowing(){
        if (mListDialog == null){
            return false;
        }
        return mListDialog.isShowing();
    }

    /**
     * 在ListView的Item上注册一个长按显示内容列表选项菜单
     * @param targetView 适用于ListView的子item
     * @param position 当前item处于ListView上的位置
     * @param id 当前的菜单编号
     * @param mOnCreateContextMenuListener 菜单创建回调接口
     * @param onActionMenuItemSelectedListener 菜单事件选中响应回调接口
     */
    public void registerForContextMenu(
            View targetView,
            int position,
            long id,
            View.OnCreateContextMenuListener mOnCreateContextMenuListener,
            ActionMenu.OnActionMenuItemSelectedListener onActionMenuItemSelectedListener) {

        mActionMenu.clear();
        AdapterView.AdapterContextMenuInfo menuInfo = new AdapterView.AdapterContextMenuInfo(targetView, position, id);
        mOnCreateContextMenuListener.onCreateContextMenu(mActionMenu, targetView, menuInfo);
        for(RXActionMenuItem menuItem :mActionMenu.getMenuItems()){
            menuItem.setContextMenuInfo(menuInfo);
        }
        show();
        mOnActionMenuItemSelectedListener = onActionMenuItemSelectedListener;
    }

    /**
     * 在View的Item上注册一个长按显示内容列表选项菜单
     * @param targetView 菜单注册view
     * @param mOnCreateContextMenuListener 菜单创建回调接口
     * @param onActionMenuItemSelectedListener 菜单事件选中响应回调接口
     */
    public void registerForContextMenu(View targetView,View.OnCreateContextMenuListener mOnCreateContextMenuListener,
                                       ActionMenu.OnActionMenuItemSelectedListener onActionMenuItemSelectedListener) {

        mOnActionMenuItemSelectedListener = onActionMenuItemSelectedListener;
        if(targetView instanceof AbsListView) {
            ((AbsListView)targetView).setOnItemLongClickListener(new AdapterViewOnItemLongClickListener(mOnCreateContextMenuListener));
            return;
        }
        targetView.setOnLongClickListener(new ViewOnLongClickListener(mOnCreateContextMenuListener));
    }

    /**
     * 在View的Item上注册一个长按显示内容列表选项菜单
     * @param targetView 菜单注册view
     * @param mOnCreateContextMenuListener 菜单创建回调接口
     * @param onActionMenuItemSelectedListener 菜单事件选中响应回调接口
     */
    public void registerListenerForView(View targetView,View.OnCreateContextMenuListener mOnCreateContextMenuListener,
                                        ActionMenu.OnActionMenuItemSelectedListener onActionMenuItemSelectedListener) {
        mActionMenu.clear();
        mOnCreateContextMenuListener.onCreateContextMenu(mActionMenu, targetView, null);
        show();
        mOnActionMenuItemSelectedListener = onActionMenuItemSelectedListener;
    }

    /**
     * 显示菜单
     * @return 当前显示的对话框
     */
    public RXListDialog show() {
        if(mOnCreateActionMenuListener != null) {
            mActionMenu.clear();
            mActionMenu = new ActionMenu();
            mOnCreateActionMenuListener.OnCreateActionMenu(mActionMenu);
        }

        if(mActionMenu.isMenuNotEmpty()) {
        }

        if(mMenuAdapter == null) {
            mMenuAdapter = new ContentMenuAdapter();
        }
        mListDialog.setAdapter(mMenuAdapter);
        mListDialog.setOnItemClickListener(this);
        mListDialog.setTitle(mActionMenu.getHeaderTitle());
        mListDialog.show();

        return mListDialog;
    }



    /**
     * 设置菜单创建时候响应的回调接口，用于对当前设置的子菜单进行设置显示文本
     * @param l 菜单绑定数据-图标回调接口
     */
    public void setOnBuildActionMenuIconListener(ActionMenu.OnBuildActionMenuIconListener l) {
        mOnBuildActionMenuIconListener = l;
    }

    /**
     * 设置菜单绑定数据-文本回调接口，用于对当前构建的子菜单进行设置图标
     * @param l 菜单绑定数据-文本回调接口
     */
    public void setOnBuildActionMenuTextListener(ActionMenu.OnBuildActionMenuTextListener l) {
        mOnBuildActionMenuTextListener = l;
    }

    /**
     * 设置菜单点击事件触发的回调接口，用于处理菜单选择处理
     * @param listener 菜单点击事件触发的回调接口
     */
    public void setOnActionMenuItemSelectedListener(ActionMenu.OnActionMenuItemSelectedListener listener) {
        mOnActionMenuItemSelectedListener = listener;
    }

    /**
     * 设置菜单创建回调接口，用于给菜单准备需要显示的菜单项目
     * @param l 菜单创建回调接口
     */
    public void setOnCreateActionMenuListener(ActionMenu.OnCreateActionMenuListener l) {
        mOnCreateActionMenuListener = l;
    }

    /**
     * 设置菜单返回键取消回调接口
     * @param listener 菜单返回键取消回调接口
     */
    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mListDialog.setOnCancelListener(listener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        RXActionMenuItem item = (RXActionMenuItem) mActionMenu.getItem(position);
        if(item.performClick()) {
            dismiss();
            return;
        }
        if(mOnActionMenuItemSelectedListener != null) {
            mOnActionMenuItemSelectedListener.OnActionMenuSelected(item, position);
        }
        dismiss();
    }

    /**
     * 菜单内容显示适配器
     */
    final private class ContentMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mActionMenu.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0L;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder mViewHolder;
            if(convertView == null || convertView.getTag() == null) {
                view = mLayoutInflater.inflate(R.layout.layout_dialog_list_item, parent, false);

                mViewHolder = new ViewHolder();
                mViewHolder.mTitle = (TextView) view.findViewById(R.id.title);
                mViewHolder.mIcon = (ImageView) view.findViewById(R.id.icon);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }

            MenuItem item = mActionMenu.getItem(position);
            if(item != null) {
                mViewHolder.mTitle.setText(item.getTitle());
                Drawable icon = item.getIcon();
                if(icon == null) {
                    mViewHolder.mIcon.setVisibility(View.GONE);
                } else {
                    mViewHolder.mIcon.setVisibility(View.VISIBLE);
                    mViewHolder.mIcon.setImageDrawable(icon);
                }
                if(mOnBuildActionMenuIconListener != null) {
                    mOnBuildActionMenuIconListener.OnBuildActionMenuIcon(mViewHolder.mIcon, item);
                }

                if(mOnBuildActionMenuTextListener != null) {
                    mOnBuildActionMenuTextListener.OnBuildActionMenuText(mViewHolder.mTitle, item);
                }
            }

            return view;
        }


        class ViewHolder {
            TextView mTitle;
            ImageView mIcon;
        }
    }

}
