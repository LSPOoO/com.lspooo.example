package com.lspooo.plugin.common.presenter;


import com.lspooo.plugin.common.presenter.presenter.BasePresenter;

/**
 * Created by Tervor on 2016/7/21 0021.
 */
public interface IBase<V,T extends BasePresenter<V>> {
    T getPresenter();
    void showPostingDialog();
    void dismissDialog();


}
