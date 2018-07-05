package com.lspooo.plugin.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lspooo.plugin.common.common.menu.search.AutoMatchKeywordEditText;
import com.lspooo.plugin.common.tools.BackwardSupportUtil;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.location.adapter.PoiListAdapter;
import com.lspooo.plugin.location.bean.NearByLocation;
import com.lspooo.plugin.location.presenter.ISearchView;
import com.lspooo.plugin.location.presenter.SearchLocationPresenter;
import com.lspooo.plugin.location.view.LoadMoreListView;

import java.util.List;

/**
 * Created by lspooo on 2018/4/16.
 */

public class SearchLocationUI extends CommonActivity<ISearchView, SearchLocationPresenter> implements ISearchView{

    private AutoMatchKeywordEditText mActionBarEditText;
    private ImageButton mClearView;

    private LoadMoreListView mListView;
    private TextView mEmptyView;
    private PoiListAdapter mAdapter;

    private String mProvince;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProvince = getIntent().getStringExtra("province");
        initView();
    }

    private void initView() {

        findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mActionBarEditText = (AutoMatchKeywordEditText) findViewById(R.id.search_view);
        mActionBarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                initDelView();
                if (mAdapter != null){
                    mAdapter.clean();
                }
                if (TextUtils.isEmpty(charSequence)) {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    mListView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.GONE);
                } else {
                    mPresenter.searchLocation(SearchLocationUI.this, mProvince,
                            charSequence.toString(), 1, true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mActionBarEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    toggleSoftInput();
                } else {
                    hideSoftKeyboard();
                }
            }
        });
        mClearView = (ImageButton) findViewById(R.id.del);
        mClearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionBarEditText.setText("");
            }
        });
        mListView = (LoadMoreListView) findViewById(R.id.search_list);
        mListView.initOnScrollListener();
        mEmptyView = (TextView) findViewById(R.id.search_empty);
        mListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                int pageIndex = (mAdapter != null) ? mAdapter.getWillPageIndex() : 1;
                String keywords = mActionBarEditText.getEditableText().toString();
                mPresenter.searchLocation(SearchLocationUI.this, mProvince, keywords, pageIndex);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearByLocation location = (NearByLocation) mAdapter.getItem(position);
                if (location == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(SearchLocationUI.this, SendLocationUI.class);
                intent.putExtra("lat", location.location.lat);
                intent.putExtra("lng", location.location.lng);
                intent.putExtra("address", location.address);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        initDelView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionBarEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActionBarEditText.requestFocus();
            }
        }, 200);
    }

    /**
     * 初始化删除按钮
     */
    private void initDelView() {
        if(mActionBarEditText.getEditableText() != null &&
                !BackwardSupportUtil.isNullOrNil(mActionBarEditText.getEditableText().toString())) {
            setDelImageResource(R.drawable.ic_close_white, getResources().getDimensionPixelSize(R.dimen.LargestPadding));
        } else {
            setDelImageResource(0, 0);
        }
    }

    /**
     * 设置清空按钮资源
     * @param resId 资源
     * @param width 大小
     */
    private void setDelImageResource(int resId, int width) {
        mClearView.setImageResource(resId);
        mClearView.setBackgroundResource(0);
        mClearView.setContentDescription("了解更多");
        ViewGroup.LayoutParams params = mClearView.getLayoutParams();
        params.width = width;
        mClearView.setLayoutParams(params);
    }


    @Override
    public SearchLocationPresenter getPresenter() {
        return new SearchLocationPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_location;
    }

    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        SearchLocationUI.this.overridePendingTransition(0, 0);
    }

    @Override
    public void onSearchResult(List<NearByLocation> result) {
        if (result == null || result.size() <= 0) {
            mListView.hideFooterViewLoading();
            if (mAdapter != null && mAdapter.getCount() > 0) {
                mListView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            } else {
                mListView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            }
            return;
        }
        mListView.showFooterViewLoading();
        mListView.setVisibility(View.VISIBLE);
        if (mAdapter == null) {
            mAdapter = new PoiListAdapter(SearchLocationUI.this);
            mListView.setAdapter(mAdapter);
        }
        mAdapter.setNearBys(result);
    }
}
