package com.lspooo.example.plugin.common.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.lspooo.example.plugin.common.R;
import com.lspooo.example.plugin.common.presenter.IBase;
import com.lspooo.example.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.example.plugin.common.tools.SystemBarTintManager;

/**
 * Created by LSP on 2017/9/24.
 */

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends AbsFragment implements IBase<V, T> {

    protected View mBaseLayoutView;
    protected View mContentView;
    private View mTransLayerView;
    private FrameLayout mContentFrameLayout;
    private SystemBarTintManager mSystemBarTintManager;
    private LayoutInflater mLayoutInflater;
    protected T mPresenter;
    private InternalReceiver internalReceiver;

    public BaseFragment() {

    }

    public BaseFragment(boolean finish) {
        super(finish);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attach((V) this);
        }
    }

    private void init() {
        mSystemBarTintManager = new SystemBarTintManager(getFragmentActivity());

        int layoutId = getLayoutId();
        mLayoutInflater = LayoutInflater.from(getFragmentActivity());
        mBaseLayoutView = mLayoutInflater.inflate(R.layout.lsp_activity, null);
        mTransLayerView = mBaseLayoutView.findViewById(R.id.ytx_trans_layer);
        mContentFrameLayout = (FrameLayout) mBaseLayoutView.findViewById(R.id.ytx_content_fl);
        ViewGroup mRootView = (ViewGroup) mBaseLayoutView.findViewById(R.id.ytx_root_view);
        if (layoutId != -1) {
            mContentView = mLayoutInflater.inflate(layoutId, null);
            mRootView.addView(mContentView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mBaseLayoutView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            getActivity().unregisterReceiver(internalReceiver);
        } catch (Exception e) {
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    public void showPostingDialog() {

    }

    @Override
    public void dismissDialog() {

    }

    protected final void registerReceiver(String[] actionArray) {
        if (actionArray == null) {
            return;
        }
        IntentFilter intentfilter = new IntentFilter();
        for (String action : actionArray) {
            intentfilter.addAction(action);
        }
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        if (getActivity() != null){
            getActivity().registerReceiver(internalReceiver, intentfilter);
        }
    }

    protected Handler getSupperHandler() {
        return mSupperHandler;
    }

    final Handler mSupperHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    };

    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            handleReceiver(context, intent);
        }
    }

    protected void handleReceiver(Context context, Intent intent) {}
    protected abstract int getLayoutId();
}
