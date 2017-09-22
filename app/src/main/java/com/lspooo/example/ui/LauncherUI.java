package com.lspooo.example.ui;

import android.os.Bundle;

import com.lspooo.example.R;
import com.lspooo.example.plugin.common.ui.AbsActivity;
import com.lspooo.example.plugin.common.ui.CommonActivity;

/**
 * Created by LSP on 2017/9/21.
 */

public class LauncherUI extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_launcher;
    }
}
