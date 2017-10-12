package com.lspooo.plugin.common.common.menu;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lspooo.plugin.common.R;

public class OverflowSubMenuHelper extends SubMenuHelperBase {

    /** 上下文*/
    private Context mContext;

    /** 布局解析器*/
    private LayoutInflater mLayoutInflater;

    /**
     * 下拉菜单创建资源回调接口
     */
    private ActionMenu.OnCreateActionMenuListener mCreateActionMenuListener;

    /**
     * 下拉菜单子项选择响应事件回调接口
     */
    private ActionMenu.OnActionMenuItemSelectedListener mActionMenuItemSelectedListener;

    /**
     * 下拉菜单图表更新回调接口
     */
    private ActionMenu.OnBuildActionMenuIconListener mBuildActionMenuIconListener;

    /**
     * 下拉菜单文本更新回调接口
     */
    private ActionMenu.OnBuildActionMenuTextListener mBuildActionMenuTextListener;

    private ActionMenu mActionMenu;

    private ActionMenuAdapter mActionMenuAdapter;
    /** 是否有自菜单*/
    private boolean mHasItems = true;


    /**
     * @param context 上下文
     */
    public OverflowSubMenuHelper(Context context) {
        super(context);

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mActionMenu = new ActionMenu();
    }

    /**
     * 设置下拉菜单创建回调接口
     * @param l 回调接口
     */
    public void setOnCreateActionMenuListener(ActionMenu.OnCreateActionMenuListener l) {
        mCreateActionMenuListener = l;
    }

    /**
     * 设置下拉菜单点击响应事件回调接口
     * @param l 回调接口
     */
    public void setOnActionMenuItemSelectedListener(ActionMenu.OnActionMenuItemSelectedListener l) {
        mActionMenuItemSelectedListener = l;
    }

    /**
     * 设置下拉菜单绘制图片更新回调接口
     * @param l 回调接口
     */
    public void setOnBuildActionMenuIconListener(ActionMenu.OnBuildActionMenuIconListener l) {
        mBuildActionMenuIconListener = l;
    }

    /**
     * 设置下拉菜单绘制文本更新回调接口
     * @param l 回调接口
     */
    public void setOnBuildActionMenuTextListener(ActionMenu.OnBuildActionMenuTextListener l) {
        mBuildActionMenuTextListener = l;
    }

    @Override
    protected BaseAdapter buildMenuAdapter() {

        if(mActionMenuAdapter == null) {
            mActionMenuAdapter = new ActionMenuAdapter();
        }
        return mActionMenuAdapter;
    }

    @Override
    public boolean tryShow() {
        mActionMenu.clear();
        mActionMenu.clearHeader();
        if(mCreateActionMenuListener != null) {
            mCreateActionMenuListener.OnCreateActionMenu(mActionMenu);
        }
        if(!TextUtils.isEmpty(mActionMenu.getHeaderTitle())) {
            mHasItems = true;
        } else {
            mHasItems = false;
        }
        return super.tryShow();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if(mHasItems && position == 0) {
            return;
        }

        if(mHasItems) {
            --position;
        }

        if(mActionMenuItemSelectedListener != null) {
            mActionMenuItemSelectedListener.OnActionMenuSelected(mActionMenu.getItem(position), position);
        }

        super.onItemClick(parent, view, position, id);
    }


    private class ActionMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(mHasItems) {
                return mActionMenu.size() + 1;
            }
            return mActionMenu.size();
        }

        @Override
        public Object getItem(int position) {

            return mActionMenu.getItem(position);
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public int getItemViewType(int position) {

            if(mHasItems && position == 0) {
                return super.getItemViewType(position);
            }
            return 1;
        }

        @Override
        public int getViewTypeCount() {
            if(mHasItems) {
                return 2;
            }
            return super.getViewTypeCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder mViewHolder;
            if(convertView == null || convertView.getTag() == null) {

                mViewHolder = new ViewHolder();
                if( mHasItems && position == 0) {
                    view = mLayoutInflater.inflate(R.layout.layout_submenu_item, parent, false);
                } else {
                    view = mLayoutInflater.inflate(R.layout.layout_submenu_item, parent, false);
                    mViewHolder.mIcon = (ImageView) view.findViewById(R.id.icon);
                    mViewHolder.mRoot = view.findViewById(R.id.root);
                }
                mViewHolder.mTitle = (TextView) view.findViewById(R.id.title);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }

            MenuItem subMenu = (MenuItem) getItem(position);
            if(subMenu != null) {

                if(subMenu.getIcon() != null) {
                    mViewHolder.mIcon.setImageDrawable(subMenu.getIcon());
                    mViewHolder.mIcon.setVisibility(View.VISIBLE);
                } else {
                    mViewHolder.mIcon.setVisibility(View.GONE);
                }
                mViewHolder.mTitle.setText(subMenu.getTitle());
            }


            if(mBuildActionMenuIconListener != null) {
                mBuildActionMenuIconListener.OnBuildActionMenuIcon(mViewHolder.mIcon, subMenu);
            }

            if(mBuildActionMenuTextListener != null) {
                mBuildActionMenuTextListener.OnBuildActionMenuText(mViewHolder.mTitle, subMenu);
            }

            return view;
        }



        class ViewHolder {
            TextView mTitle;
            View mRoot;
            ImageView mIcon;
        }
    }
}
