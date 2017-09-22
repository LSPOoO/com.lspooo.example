package com.lspooo.example.ui;

import android.os.Bundle;
import android.view.MenuItem;

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
        hideHomeActionMenu();
        setActionBarTitle(getString(R.string.app_name));
        setActionMenuItem(0, R.drawable.ic_more_black, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_launcher;
    }
}
