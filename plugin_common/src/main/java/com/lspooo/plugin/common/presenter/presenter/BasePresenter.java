package com.lspooo.plugin.common.presenter.presenter;

import java.lang.ref.WeakReference;

/**
 * Created by Tervor on 2016/7/20 0020.
 */
public abstract class BasePresenter<T> {


    protected T mView;

    public WeakReference<T> reference;

    public void attach(T mView) {
        reference = new WeakReference<T>(mView);
        this.mView = reference.get();
    }

    public void detachView() {
        if(reference!=null){
            reference.clear();
            reference =null;
        }
        if (mView != null) {
            mView = null;
        }
    }

    public boolean isViewAttached() {
        return  reference!=null && reference.get()!=null;
    }

}
