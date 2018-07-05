package com.lspooo.plugin.statistics.ui.presenter;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.statistics.dao.bean.DBTeaEmployeeTools;
import com.lspooo.plugin.statistics.dao.bean.DBTeaRecordTools;
import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lspooo on 2018/1/25.
 */

public class StatisticsPresenter extends BasePresenter<IStatisticsView> {


    public void startStatistics(final String startTime, final String endTime){
        Observable.create(new Observable.OnSubscribe<List<TeaEmployee>>() {
            @Override
            public void call(Subscriber<? super List<TeaEmployee>> subscriber) {
                //获取我们所有的员工
                long sumCount = 0;
                Double sumWeight = 0.0;

                List<TeaEmployee> result = DBTeaEmployeeTools.getInstance().query();
                if (result != null && result.size() > 0){
                    for (TeaEmployee employee : result) {
                        List<TeaRecord> teaRecordList = DBTeaRecordTools.getInstance().queryTeaRecord(employee.getId(), startTime, endTime);
                        if (teaRecordList != null && teaRecordList.size() > 0){
                            Double totalWeight = 0.0;
                            for (TeaRecord record : teaRecordList) {
                                BigDecimal a = new BigDecimal(String.valueOf(record.getWeight()));
                                BigDecimal b = new BigDecimal(String.valueOf(totalWeight));
                                BigDecimal c = b.add(a);
                                totalWeight = c.doubleValue();
                            }
                            employee.setTotalWeight(totalWeight);
                            employee.setCount(teaRecordList.size());

                        } else {
                            employee.setTotalWeight(0.0);
                            employee.setCount(0);
                        }
                    }
                }
                subscriber.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<TeaEmployee>>() {
                    @Override
                    public void call(List<TeaEmployee> result) {
                        if (mView != null){
                            mView.onStatisticsResult(result);
                        }
                    }
                });
    }
}
