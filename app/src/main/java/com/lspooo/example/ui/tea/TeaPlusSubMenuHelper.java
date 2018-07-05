package com.lspooo.example.ui.tea;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.lspooo.example.R;
import com.lspooo.plugin.common.common.menu.AbsSubMenuHelper;
import com.lspooo.plugin.common.common.menu.SubMenu;
import com.lspooo.plugin.common.ui.AbsActivity;
import com.lspooo.plugin.location.LocationUI;
import com.lspooo.plugin.location.SendLocationUI;
import com.lspooo.plugin.statistics.ui.AddEmployeeActivity;
import com.lspooo.plugin.statistics.ui.StatisticsActivity;
import com.lspooo.plugin.statistics.ui.TeaDateActivity;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class TeaPlusSubMenuHelper extends AbsSubMenuHelper {

    private List<Integer> title = new ArrayList<>();
    private List<Integer> icon = new ArrayList<>();
    private List<Integer> menuId = new ArrayList<>();
    private Context ctx;

    /**
     * 构造方法
     *
     * @param activity 上下文
     */
    public TeaPlusSubMenuHelper(AbsActivity activity) {
        super(activity);
        ctx = activity;
        init();
    }

    private void init() {
        title.add(R.string.add_tea_employee);
        title.add(R.string.tea_date);
        title.add(R.string.tea_statistics);
        icon.add(0);
        icon.add(0);
        icon.add(0);
        menuId.add(1);
        menuId.add(2);
        menuId.add(3);
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
                Intent intent = new Intent(context, AddEmployeeActivity.class);
                context.startActivity(intent);
                break;
            }
            case 2:{
                Intent intent = new Intent(context, TeaDateActivity.class);
                context.startActivity(intent);
                break;
            }
            case 3: {
                Intent intent = new Intent(context, StatisticsActivity.class);
                context.startActivity(intent);
                break;
            }

            case 4:
                break;
        }
    }


}
