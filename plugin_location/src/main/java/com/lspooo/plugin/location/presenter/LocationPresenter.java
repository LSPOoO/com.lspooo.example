package com.lspooo.plugin.location.presenter;

import android.content.Context;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.location.bean.NearByLocation;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lspooo on 2018/4/16.
 */

public class LocationPresenter extends BasePresenter<LocationView>{

    /***
     * 请求查询周边的位置信息
     *
     * @param latitude  经度
     * @param longitude 维度
     */
    public void startRequestPoiMessage(Context context, float latitude, float longitude, String address) {
        final NearByLocation mNearByLocation = new NearByLocation(address, new Location(latitude, longitude), true);
        Location location = new Location(latitude, longitude);
        TencentSearch tencentSearch = new TencentSearch(context);
        Geo2AddressParam geo2AddressParam = new Geo2AddressParam().location(location).get_poi(true);
        tencentSearch.geo2address(geo2AddressParam, new HttpResponseListener() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, BaseObject arg2) {
                if (arg2 == null) {
                    if (mView != null) {
                        mView.onRequestPoiResult(false, null, null);
                    }
                    return;
                }
                Geo2AddressResultObject obj = (Geo2AddressResultObject) arg2;
                if (obj.result != null && obj.result.formatted_addresses != null) {
                    mNearByLocation.address = obj.result.formatted_addresses.recommend;
                }
                List<NearByLocation> nearBys = new ArrayList<NearByLocation>();
                if (obj.result != null && obj.result.pois != null) {
                    for (Geo2AddressResultObject.ReverseAddressResult.Poi poi : obj.result.pois) {
                        NearByLocation location = new NearByLocation();
                        location.title = poi.title;
                        location.address = poi.address;
                        location.location = poi.location;
                        nearBys.add(location);
                    }
                }
                if (mView != null) {
                    mView.onRequestPoiResult(true, mNearByLocation, nearBys);
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                if (mView != null) {
                    mView.onRequestPoiResult(false, null, null);
                }
            }
        });
    }

}
