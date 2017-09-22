package com.lspooo.example.plugin.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by LSP on 2017/9/21.
 */

public abstract class CommonActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean hasActionBar() {
        return true;
    }

    @Override
    public boolean buildActionBarPadding() {
        return false;
    }
}
