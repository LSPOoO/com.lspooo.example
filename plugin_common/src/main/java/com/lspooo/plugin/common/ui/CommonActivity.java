package com.lspooo.plugin.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lspooo.plugin.common.presenter.IBase;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;

/**
 * Created by LSP on 2017/9/21.
 */

public abstract class CommonActivity<V, T extends BasePresenter<V>> extends BaseActivity implements IBase<V, T> {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();

        if (mPresenter != null) {
            mPresenter.attach((V) this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public boolean buildActionBarPadding() {
        return false;
    }

    @Override
    public boolean isActionBarTitleMiddle() {
        return false;
    }
}
