package com.lspooo.example.plugin.common.presenter;


import com.lspooo.example.plugin.common.presenter.presenter.BasePresenter;

/**
 * Created by Tervor on 2016/7/21 0021.
 */
public interface IBase<V,T extends BasePresenter<V>> {
    T getPresenter();
    void showPostingDialog();
    void dismissDialog();


}
