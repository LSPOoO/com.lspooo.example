package com.lspooo.example.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.lspooo.example.R;
import com.lspooo.plugin.common.common.menu.AbsSubMenuHelper;
import com.lspooo.plugin.common.common.menu.SubMenu;
import com.lspooo.plugin.common.ui.AbsActivity;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class PlusSubMenuHelper extends AbsSubMenuHelper {

    private List<Integer> title = new ArrayList<>();
    private List<Integer> icon = new ArrayList<>();
    private List<Integer> menuId = new ArrayList<>();
    private Context ctx;

    /**
     * 构造方法
     *
     * @param activity 上下文
     */
    public PlusSubMenuHelper(AbsActivity activity) {
        super(activity);
        ctx = activity;
        init();
    }

    private void init() {
        title.add(R.string.add_tea_employee);
        icon.add(0);
        menuId.add(1);
    }

    @Override
    public int getCount() {
        return title != null ? title.size() : 0;
    }

    @Override
    public SubMenu onBuildSubMenu(Context context, int position) {
        return new SubMenu(menuId.get(position), icon.get(position),
                context.getString(title.get(position)), null);
    }

    @Override
    public void onMenuClick(final Context context, SubMenu menu) {
        switch (menu.getMenuId()) {
            case 1:{
                Intent intent = new Intent(context, ShareExampleActivity.class);
                context.startActivity(intent);
                break;
            }
            default:
                break;
        }
    }


}
