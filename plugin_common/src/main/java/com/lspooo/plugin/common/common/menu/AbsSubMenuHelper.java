package com.lspooo.plugin.common.common.menu;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lspooo.plugin.common.LSPApplicationContext;
import com.lspooo.plugin.common.R;
import com.lspooo.plugin.common.ui.AbsActivity;

public abstract class AbsSubMenuHelper extends SubMenuHelperBase {

    private static final String TAG = "RongXin.AbsSubMenuHelper";

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private SubMenuAdapter mPlusSubMenuAdapter;

    private SparseArray<SubMenu> mSparseArray;

    /**
     *
     */
    public AbsSubMenuHelper(AbsActivity activity) {
        super(activity);
        mContext = activity;
        mLayoutInflater = LayoutInflater.from(activity);

        enableStatusBar();
    }

    @Override
    protected BaseAdapter buildMenuAdapter() {

        if(mPlusSubMenuAdapter == null) {
            mPlusSubMenuAdapter = new SubMenuAdapter();
        }
        return mPlusSubMenuAdapter;
    }

    @Override
    public boolean tryShow() {

        if(mSparseArray == null) {
            mSparseArray = new SparseArray<>();
        }
        mSparseArray.clear();
        for(int i = 0 ; i < getCount() ; i++) {
            SubMenu createMenu = createMenu(i);
            mSparseArray.put(i, createMenu);
        }

        return super.tryShow();
    }

    /**
     * 创建菜单
     * @param position 菜单所在位置
     * @return 菜单
     */
    private SubMenu createMenu(int position) {
        Context context = LSPApplicationContext.getContext();
        return onBuildSubMenu(context , position);
    }

    public abstract int getCount();

    public abstract SubMenu onBuildSubMenu(Context context, int position);

    public abstract void onMenuClick(Context context, SubMenu menu);


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if(mContext instanceof AbsActivity) {
            ((AbsActivity) mContext).supportInvalidateOptionsMenu();
        }

        SubMenu subMenu = mSparseArray.get(position);
        onMenuClick(mContext , subMenu);
        super.onItemClick(parent, view, position, id);
    }

    public Context getContext() {
        return mContext;
    }


    private class SubMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSparseArray.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder mViewHolder;
            if(convertView == null || convertView.getTag() == null) {
                view = mLayoutInflater.inflate(R.layout.layout_submenu_item, parent, false);

                mViewHolder = new ViewHolder();
                mViewHolder.mTitle = (TextView) view.findViewById(R.id.title);
                mViewHolder.mNewTips = (TextView) view.findViewById(R.id.new_tips);
                mViewHolder.mUnreadCount = (TextView) view.findViewById(R.id.unread_count);
                mViewHolder.mIcon = (ImageView) view.findViewById(R.id.icon);
                mViewHolder.mNewDot = (ImageView) view.findViewById(R.id.new_dot);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder) view.getTag();
            }

            SubMenu subMenu = mSparseArray.get(position);
            if(subMenu != null) {
                if(subMenu.getIcon() > 0) {
                    mViewHolder.mIcon.setImageResource(subMenu.getIcon());
                    mViewHolder.mIcon.setVisibility(View.VISIBLE);
                } else {
                    mViewHolder.mIcon.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(subMenu.getDesc())) {
                    mViewHolder.mIcon.setContentDescription(subMenu.getDesc());
                }
                mViewHolder.mTitle.setText(subMenu.getTitle());
            }

//            if(getCount() - 1 == position) {
//                view.setBackgroundResource(R.drawable.submenu_item_selector_no_divider);
//            } else {
//                view.setBackgroundResource(R.drawable.ytx_comm_list_item_selector);
//            }
            return view;
        }



        class ViewHolder {
            TextView mTitle;
            TextView mNewTips;
            TextView mUnreadCount;
            ImageView mIcon;
            ImageView mNewDot;
        }
    }
}
