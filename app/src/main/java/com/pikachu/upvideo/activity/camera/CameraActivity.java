package com.pikachu.upvideo.activity.camera;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.view.PreviewView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.google.android.material.appbar.AppBarLayout;
import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.util.AppInfo;
import com.pikachu.upvideo.util.base.BaseActivity;
import com.pikachu.upvideo.util.state.PKStatusBarTools;
import com.pikachu.upvideo.util.tools.ToolCameraX;
import com.pikachu.upvideo.util.tools.ToolGaoDe;
import com.pikachu.upvideo.util.tools.ToolTimer;
import com.pikachu.upvideo.util.view.CameraBtView;
import com.pikachu.upvideo.util.view.QMUIRadiusImageView;

import java.io.File;

public class CameraActivity extends BaseActivity implements ToolTimer.OnTimeRunListener,
        AMapLocationListener, ToolCameraX.OnVideoSavedCallback {

    private PreviewView caPre;
    private LinearLayout caLi1;
    private TextView caText1;
    private QMUIRadiusImageView caQmui1;
    private CameraBtView caCameraBt1;
    private CameraBtView caCameraBt2;
    /*private Toolbar barToolbar;*/
    private ToolTimer timer;
    private ToolCameraX cameraX;
    private ToolGaoDe toolGaoDeTools;
    private TextView textGaoDe;
    private boolean dStop = false;
    private VideoUpJson videoUpJson;
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera/*, R.id.bar_view*/);
        initView();
        init();
    }

    private void init() {




        //根目项目数据
        videoUpJson = getSerializableExtra(AppInfo.START_ACTIVITY_KEY_1, VideoUpJson.class);
        type = getIntExtra(AppInfo.START_ACTIVITY_KEY_2, 1);

        //添加bar
        //setSupportActionBar(barToolbar);
        //setHead(true,videoUpJson.getProjectName() ,null, this::finish);

        //开始预览
        cameraX = new ToolCameraX(this, caPre, this);
        //cameraX.setSeekBar(seekBar); //设置变焦bar
        //初始高德定位
        toolGaoDeTools = ToolGaoDe.getGaoDeTools(this, this);
        timer = ToolTimer.getTimer(this, this);


        caCameraBt1.setOnClickListener((view, isSed) -> {
            if (isSed) {
                // 开始录像
                timer.run(); // 开始计时
                toolGaoDeTools.start(); // 开始定位
                cameraX.startVideo(); // 开始录像
                caLi1.setVisibility(View.VISIBLE);
                showToast("开始录像");

            } else {
                //结束录像
                timer.afresh();
                // 结束计时
                toolGaoDeTools.stop(); // 结束定位
                cameraX.stopVideo(); // 结束录像/并保存
                caLi1.setVisibility(View.GONE);
                showToast("结束录像");
            }
            dStop = false; // 用于弹框退出
            return true;
        });

        caCameraBt2.setOnClickListener((view, isSed) -> {
            if (caCameraBt1.isSed()) {
                timer.afresh();
                timer.run();
                showToast("在录像");
            } else
                showToast("没录像");
            //showToast("分段");

            return true;
        });

    }

    @Override
    public PKStatusBarTools pkStatusBarTools() {
        PKStatusBarTools pkStatusBarTools = PKStatusBarTools.with(this).hideSTS().hideNON();
        pkStatusBarTools.init();
        return pkStatusBarTools;
    }


    private void initView() {
        /*barToolbar = findViewById(R.id.bar_toolbar);*/
        caPre = findViewById(R.id.ca_pre);
        caLi1 = findViewById(R.id.ca_li1);
        caText1 = findViewById(R.id.ca_text1);
        caQmui1 = findViewById(R.id.ca_qmui1);
        caCameraBt1 = findViewById(R.id.ca_cameraBt1);
        caCameraBt2 = findViewById(R.id.ca_cameraBt2);
        textGaoDe = findViewById(R.id.gaodeInfo);

    }


    @Override
    public void runTime(String timeStr) {
        caText1.setText(timeStr);
    }

    @Override
    public void stopTime(String timeStr) {
        caText1.setText(timeStr);
    }

    //定位返回
    @SuppressLint("SimpleDateFormat")
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        StringBuilder stringBuffer = new StringBuilder();
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            stringBuffer.append(VideoUpJson.aMapLocation2MapInfo(aMapLocation).toString());
        } else {
            stringBuffer.append("定位失败,请打开GPS定位");
        }
        textGaoDe.setText(stringBuffer.toString());
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

            if (caCameraBt1.isSed()) {

                new AlertDialog.Builder(this)
                        .setTitle("束录未结束")
                        .setMessage("还没有结束录像哦，是否保存退出")
                        .setPositiveButton("保存", (dialog, which) -> {

                            timer.afresh(); // 结束计时
                            toolGaoDeTools.stop(); // 结束定位
                            cameraX.stopVideo(); // 结束录像/并保存
                            caLi1.setVisibility(View.GONE);

                            dStop = true;
                        }).show();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    //视频保存成功
    @Override
    public void onVideoSaved(@NonNull File file, String filePath) {
        showToast("已保存到： " + filePath);

        if (dStop) finish();
    }


    //视频保存失败
    @Override
    public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause,
                        String filePath) {
        showToast("保存失败： " + message);

        if (dStop) finish();
    }


}