package com.lspooo.plugin.location.adapter;

import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lspooo.plugin.location.bean.NearByLocation;
import com.lspooo.plugin.location.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 容联•云通讯
 * @version 5.2.0
 * @since 2016-05-19
 */
public class PoiListAdapter extends BaseAdapter {

    private int pageIndex = 1;
    private int mSelectedPosition = -1;
    private Context mContext;
    private List<NearByLocation> mNearBys = new ArrayList<>();

    public PoiListAdapter(Context ctx) {
        mContext = ctx;
    }

    @Override
    public int getCount() {
        return mNearBys.size();
    }

    @Override
    public Object getItem(int position) {
        return mNearBys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setNearBys(List<NearByLocation> pois){
        for(NearByLocation location : pois) {
            mNearBys.add(location);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置当前的地理位置信息
     * @param location 地理位置信息
     */
    public final void setCurrentLocation(NearByLocation location) {
        if (this.mNearBys.size() >= 0) {
            location.choice = true;
            mSelectedPosition = 0;
            this.mNearBys.add(0, location);
            notifyDataSetChanged();
        }
    }

    /**
     * 选中的地理位置
     * @param position 地理位置
     *
     */
    public void setSelectedPosition(int position) {
        mSelectedPosition = (position < getCount()) ? position : getCount() - 1;
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public final void clean() {
        pageIndex = 1;
        mSelectedPosition = -1;
        mNearBys.clear();
    }

    public int getWillPageIndex() {
        pageIndex = pageIndex + 1;
        return pageIndex;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Thread.currentThread().getId();
        Looper.getMainLooper().getThread().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view ;
        ViewHolder mViewHolder ;
        if(convertView == null || convertView.getTag() == null) {
            view = View.inflate(mContext, R.layout.layout_poi_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mTipsChecker = (ImageView) view.findViewById(R.id.tip_view);
            mViewHolder.mTitle = (TextView) view.findViewById(R.id.title);
            mViewHolder.mSubTitle = (TextView) view.findViewById(R.id.subtitle);
            view.setTag(mViewHolder);
        } else {
            view = convertView;
            mViewHolder = (ViewHolder) view.getTag();
        }
        NearByLocation location = (NearByLocation) getItem(position);
        if(location != null) {
            mViewHolder.mTitle.setText(location.title);
            mViewHolder.mSubTitle.setText(location.address);

            boolean mSelectPosition = (mSelectedPosition == position);
            if(location.choice) {
                mViewHolder.mSubTitle.setVisibility(View.GONE);
                mViewHolder.mTitle.setText(location.address);
            } else {
                mViewHolder.mSubTitle.setVisibility(View.VISIBLE);
            }
            mViewHolder.mTipsChecker.setVisibility(mSelectPosition ? View.VISIBLE : View.INVISIBLE);
        }
        return view;
    }


    static class ViewHolder {
        ImageView mTipsChecker;
        TextView mTitle;
        TextView mSubTitle;
    }
}
