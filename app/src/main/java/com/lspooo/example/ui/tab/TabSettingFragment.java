package com.lspooo.example.ui.tab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.lspooo.example.R;
import com.lspooo.plugin.camera.CameraActivity;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.tools.EasyPermissionsEx;
import com.lspooo.plugin.common.ui.TabFragment;
import com.lspooo.plugin.location.SendLocationUI;

/**
 * Created by LSP on 2017/9/24.
 */

public class TabSettingFragment extends TabFragment {

    private static final int PERMISSIONS_REQUEST_LOCATION = 0x15;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        findViewById(R.id.location_setting_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocation();
            }
        });
        findViewById(R.id.camera_setting_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);

            }
        });
    }

    private void startLocation(){
        if (EasyPermissionsEx.hasPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})) {
            Intent intent = new Intent(getActivity(), SendLocationUI.class);
            startActivity(intent);
        } else {
            EasyPermissionsEx.requestPermissions(TabSettingFragment.this,
                    getString(R.string.rationaleLocation),
                    PERMISSIONS_REQUEST_LOCATION,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onTabFragmentClick() {}

    @Override
    public void onReleaseTabUI() {}

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_setting;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startLocation();
            }
        }
    }
}
