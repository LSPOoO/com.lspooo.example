package com.lspooo.plugin.statistics.ui.presenter;

import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.statistics.dao.bean.DBTeaRecordTools;
import com.lspooo.plugin.statistics.dao.bean.TeaDate;
import com.lspooo.plugin.statistics.dao.bean.TeaRecord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lspooo on 2018/3/26.
 */

public class TeaDatePresenter extends BasePresenter<ITeaDateView>{

    public void startListTeaDate(){
        Observable.create(new Observable.OnSubscribe<List<TeaDate>>() {
            @Override
            public void call(Subscriber<? super List<TeaDate>> subscriber) {
                List<TeaRecord> recordList = DBTeaRecordTools.getInstance().queryTeaDate();
                if (recordList != null && recordList.size() > 0) {
                    String date = "";
                    List<TeaDate> dateList = new ArrayList<>();
                    for (TeaRecord teaRecord : recordList) {
                        TeaDate teaDate;
                        if (!date.equals(teaRecord.getTime())) {
                            //新建一个采茶时间
                            date = teaRecord.getTime();
                            teaDate = new TeaDate();
                            dateList.add(teaDate);
                        } else {
                            teaDate = dateList.get(dateList.size() - 1);
                        }
                        teaDate.setTime(date);
                        teaDate.setTotalCount(teaDate.getTotalCount() + 1);
                        BigDecimal a = new BigDecimal(String.valueOf(teaDate.getWeight()));
                        BigDecimal b = new BigDecimal(String.valueOf(teaRecord.getWeight()));
                        BigDecimal c = b.add(a);
                        Double totalWeight = c.doubleValue();
                        teaDate.setWeight(totalWeight);
                    }
                    subscriber.onNext(dateList);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<TeaDate>>() {
                    @Override
                    public void call(List<TeaDate> result) {
                        if (mView != null) {
                            mView.onLoadTeaDateResult(result);
                        }
                    }
                });
    }
}
