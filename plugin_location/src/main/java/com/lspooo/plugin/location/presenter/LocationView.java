package com.lspooo.plugin.location.presenter;

import com.lspooo.plugin.location.bean.NearByLocation;

import java.util.List;

/**
 * Created by lspooo on 2018/4/16.
 */

public interface LocationView {

    void onRequestPoiResult(boolean success, NearByLocation location, List<NearByLocation> result);
}
