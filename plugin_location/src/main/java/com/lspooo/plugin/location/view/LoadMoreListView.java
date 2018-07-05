package com.lspooo.plugin.location.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.lspooo.plugin.location.R;

/**
 * 自定义ListView底部加载控件
 * @author 容联•云通讯
 * @version 5.2.0
 * @since 2016-05-18
 */
public class LoadMoreListView extends ListView {

    public static final String TAG = "RongXin.RXLoadMoreListView";

    /**加载更多*/
    public View mFooterView;
    private TextView mFooterTipsView ;
    private boolean mScrollTop = false;
    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreListView(Context context) {
        super(context);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化ListView Footer加载显示控件
     */
    private void init() {
        if(mFooterView == null) {
            initView();
            addFooterView(mFooterView);
            mFooterTipsView.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化Footer加载布局
     */
    private void initView() {
        mFooterView = View.inflate(getContext(), R.layout.layout_load_footer, null);
        mFooterTipsView = (TextView) mFooterView.findViewById(R.id.footer_tips);
        mFooterTipsView.setVisibility(View.GONE);
    }

    /**
     * 初始化滑动事件监听回调
     */
    public void initOnScrollListener() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if ((getLastVisiblePosition() == getCount() - 1) && (mOnLoadMoreListener != null)) {
                    mOnLoadMoreListener.onLoadMore();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mScrollTop = firstVisibleItem == 0 && getChildAt(0) != null && getChildAt(0).getTop() == getPaddingTop();
            }

        });
    }

    /**
     * 隐藏正在加载
     */
    public void hideFooterViewLoading () {
        if(mFooterView != null) {
            mFooterView.setVisibility(View.GONE);
            mFooterTipsView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示正在加载中
     */
    public void showFooterViewLoading () {
        mFooterView.setVisibility(View.VISIBLE);
        mFooterTipsView.setVisibility(View.VISIBLE);
    }

    /**
     * 是否滑动到最开始位置
     * @return 最开始位置
     */
    public boolean getScroll2Top() {
        return mScrollTop;
    }

    /**
     * 设置Footer描述
     * @param tips 描述
     */
    public void setFooterTips(String tips) {
        mFooterTipsView.setText(tips);
    }

    /**
     * 设置Footer点击事件
     * @param listener 点击事件
     */
    public void setOnFootrClickListener(OnClickListener listener) {
        mFooterTipsView.setOnClickListener(listener);
    }

    /**
     * 设置正在加载回调接口
     * @param callback 回调接口
     */
    public void setOnLoadMoreListener(OnLoadMoreListener callback) {
        mOnLoadMoreListener = callback;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
