package com.lspooo.plugin.location.presenter;

import android.content.Context;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.tools.BackwardSupportUtil;
import com.lspooo.plugin.location.bean.NearByLocation;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.result.SearchResultObject;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lspooo on 2018/4/16.
 */

public class SearchLocationPresenter extends BasePresenter<ISearchView>{


    /**
     * 关键字提示
     * @param keyword 搜索关键字
     */
    public void searchLocation(Context context, String mProvince, String keyword, int pageIndex) {
        searchLocation(context, mProvince, keyword, pageIndex, false);
    }

    /**
     *
     * @param context
     * @param mProvince
     * @param keyword
     * @param pageIndex
     * @param autoExtend
     */
    public void searchLocation(Context context, String mProvince, String keyword, int pageIndex, boolean autoExtend){
        if (keyword.trim().length() == 0) {
            return;
        }
        if (BackwardSupportUtil.isNullOrNil(mProvince)) {
            mProvince = "北京";
        }
        SearchParam.Region boundary = new SearchParam.Region().poi(mProvince).autoExtend(autoExtend);
        TencentSearch tencentSearch = new TencentSearch(context);
        SearchParam searchParam = new SearchParam().keyword(keyword)
                .page_index(pageIndex)
                .orderby(true)
                .page_size(20)
                .boundary(boundary);
        tencentSearch.search(searchParam, new HttpResponseListener() {
            @Override
            public void onSuccess(int i, Header[] headers, BaseObject baseObject) {
                SearchResultObject resultObject = (SearchResultObject) baseObject;
                if (resultObject == null || resultObject.data == null || resultObject.data.size() == 0) {
                    if (mView != null) {
                        mView.onSearchResult(null);
                    }
                    return;
                }
                final List<NearByLocation> nearBys = new ArrayList<>();
                for (SearchResultObject.SearchResultData suggestionData : resultObject.data) {
                    NearByLocation nearBy = new NearByLocation();
                    nearBy.title = suggestionData.title;
                    nearBy.address = suggestionData.address;
                    nearBy.location = suggestionData.location;
                    nearBys.add(nearBy);
                }
                if (mView != null) {
                    mView.onSearchResult(nearBys);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }
        });
    }
}
