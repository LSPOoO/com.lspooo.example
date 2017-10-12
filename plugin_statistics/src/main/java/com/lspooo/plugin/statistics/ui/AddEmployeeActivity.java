package com.lspooo.plugin.statistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.lspooo.plugin.common.common.BroadcastIntent;
import com.lspooo.plugin.common.common.toast.ToastUtil;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.ui.CommonActivity;
import com.lspooo.plugin.statistics.R;
import com.lspooo.plugin.statistics.dao.bean.DBTeaEmployeeTools;
import com.lspooo.plugin.statistics.dao.bean.TeaEmployee;

/**
 * Created by LSP on 2017/10/11.
 */

public class AddEmployeeActivity extends CommonActivity{

    private EditText nameEt;
    private TeaEmployee employee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle(getString(R.string.add_tea_employee));
        setActionMenuItem(0, R.drawable.ic_done_white, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (TextUtils.isEmpty(nameEt.getText().toString().trim())){
                    ToastUtil.info("请填写采茶工姓名！");
                    return true;
                }
                if (employee == null){
                    employee = new TeaEmployee();
                }
                employee.setName(nameEt.getText().toString().trim());
                DBTeaEmployeeTools.getInstance().insert(employee);
                ToastUtil.success("添加成功！");
                //通知刷新
                Intent intent = new Intent(BroadcastIntent.ACTION_SYNC_TEA_EMPLOYEE);
                sendBroadcast(intent);
                hideSoftKeyboard();
                finish();
                return true;
            }
        });
        initView();
    }

    private void initView() {
        nameEt = (EditText) findViewById(R.id.nameEt);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_employee;
    }
}
