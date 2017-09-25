package com.lspooo.example.plugin.common.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.lspooo.example.plugin.common.presenter.presenter.BasePresenter;

public abstract class TabFragment<V,T extends BasePresenter<V>> extends BaseFragment {

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

        }
    };

    public Handler getHandler() {
        return handler;
    }


    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        onReleaseTabUI();
        super.onDestroy();
    }


    protected T mPresenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        if (mPresenter != null ) {
            mPresenter.attach((V) this);
        }
    }

    abstract public T getPresenter();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 当前TabFragment被点击
     */
    public abstract void onTabFragmentClick();

    /**
     * 当前TabFragment被释放
     */
    public abstract void onReleaseTabUI();

}
