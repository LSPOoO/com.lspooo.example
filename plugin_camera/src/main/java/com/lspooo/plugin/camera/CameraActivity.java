package com.lspooo.plugin.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lspooo.plugin.camera.listener.ClickListener;
import com.lspooo.plugin.camera.listener.ErrorListener;
import com.lspooo.plugin.camera.listener.JCameraListener;
import com.lspooo.plugin.camera.util.DeviceUtil;
import com.lspooo.plugin.camera.util.FileUtil;
import com.lspooo.plugin.common.presenter.presenter.BasePresenter;
import com.lspooo.plugin.common.tools.EasyPermissionsEx;
import com.lspooo.plugin.common.ui.CommonActivity;

import java.io.File;

public class CameraActivity extends CommonActivity {

    private static final int PERMISSIONS_REQUEST_VOICE = 0x1;
    public static final int PERMISSIONS_REQUEST_CAMERA = 0x2;

    private View permissionLayout;
    private TextView cameraPermissionBtn;
    private TextView microphonePermissionBtn;
    private JCameraView jCameraView;

    private boolean cameraPermission = false;
    private boolean voicePermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initPermissionView();
    }

    private void initPermissionView() {
        permissionLayout = findViewById(R.id.permission_layout);
        microphonePermissionBtn = (TextView) findViewById(R.id.microphone_permission_btn);
        microphonePermissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyPermissionsEx.requestPermissions(CameraActivity.this,
                        getString(R.string.rationaleVoice),
                        PERMISSIONS_REQUEST_VOICE,
                        new String[]{Manifest.permission.RECORD_AUDIO});
            }
        });
        cameraPermissionBtn = (TextView) findViewById(R.id.camera_permission_btn);
        cameraPermissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyPermissionsEx.requestPermissions(CameraActivity.this,
                        getString(R.string.rationaleCamera),
                        PERMISSIONS_REQUEST_CAMERA,
                        new String[]{Manifest.permission.CAMERA});
            }
        });
        checkNeedPermission();
    }

    private void checkNeedPermission(){
        //检测相机和录音权限
        if (EasyPermissionsEx.hasPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO})) {
            voicePermission = true;
        }
        if (EasyPermissionsEx.hasPermissions(this, new String[]{Manifest.permission.CAMERA})) {
            cameraPermission = true;
        }
        if (voicePermission && cameraPermission) {
            permissionLayout.setVisibility(View.GONE);
            initCameraView();
        } else {
            permissionLayout.setVisibility(View.VISIBLE);
            microphonePermissionBtn.setText(voicePermission ? R.string.microphone_permission_opened : R.string.open_microphone_permission);
            cameraPermissionBtn.setText(cameraPermission ? R.string.camera_permission_opened : R.string.open_camera_permission);
        }
    }

    private void initCameraView() {
        jCameraView = new JCameraView(this);
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        jCameraView.setTip("JCameraView Tip");
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setErrorListener(new ErrorListener() {
            @Override
            public void onError() {
                Intent intent = new Intent();
                setResult(103, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {

            }
        });
        //JCameraView监听
        jCameraView.setJCameraListener(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                String path = FileUtil.saveBitmap("JCamera", bitmap);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(101, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                String path = FileUtil.saveBitmap("JCamera", firstFrame);
                Log.i("CJT", "url = " + url + ", Bitmap = " + path);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(101, intent);
                finish();
            }

            @Override
            public void cancel() {
                finish();
            }
        });
        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        container.addView(jCameraView);
        jCameraView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (jCameraView != null) {
            jCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (jCameraView != null) {
            jCameraView.onPause();
        }
    }

    @Override
    public boolean hasActionBar() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_VOICE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkNeedPermission();
            } else {
                if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, Manifest.permission.RECORD_AUDIO)) {
                    EasyPermissionsEx.goSettings2Permissions(this,
                            getString(R.string.goSettingsRationaleVoice),
                            getString(R.string.set),
                            0x1001);
                }
            }
        } else if (requestCode == PERMISSIONS_REQUEST_CAMERA){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkNeedPermission();
            } else {
                if (EasyPermissionsEx.somePermissionPermanentlyDenied(this, Manifest.permission.CAMERA)) {
                    EasyPermissionsEx.goSettings2Permissions(this,
                            getString(R.string.goSettingsRationaleCamera),
                            getString(R.string.set),
                            0x1001);
                }
            }
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }
}
