package com.lspooo.plugin.location.presenter;

import com.lspooo.plugin.location.bean.NearByLocation;

import java.util.List;

/**
 * Created by lspooo on 2018/4/16.
 */

public interface ISearchView {

    void onSearchResult(List<NearByLocation> result);
}
