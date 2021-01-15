package com.pikachu.upvideo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.camera.view.PreviewView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.init.CameraX;
import com.pikachu.upvideo.init.GaoDe;
import com.pikachu.upvideo.tools.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener, BaseActivity.OnPermissionListener, AMapLocationListener, RadioGroup.OnCheckedChangeListener {

    private PreviewView pre;
    private Button mainBt1;
    private Button mainBt2;
    private Button mainBt3;
    private TextView textGaoDe;
    private SeekBar seekBar;
    private RadioGroup radioGroup;



    //预览/录像
    private CameraX cameraX;
    //高德
    private GaoDe gaoDeTools;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    @SuppressLint("WrongConstant")
    private void init() {
        //权限申请
        sendPermission(this, Manifest.permission.CAMERA,//相机权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE,//写入SD权限
                Manifest.permission.READ_EXTERNAL_STORAGE,//读取SD权限
                Manifest.permission.RECORD_AUDIO,//录音权限
                Manifest.permission.ACCESS_COARSE_LOCATION,//网络定位
                Manifest.permission.ACCESS_FINE_LOCATION,//GPS定位
                Manifest.permission.ACCESS_NETWORK_STATE,//运营商网络定位
                Manifest.permission.ACCESS_WIFI_STATE,//wifi定位
                Manifest.permission.CHANGE_WIFI_STATE,//获取Wifi信息
                Manifest.permission.INTERNET,//联网权限
                Manifest.permission.READ_PHONE_STATE,//手机状态
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);//申请调用A-GPS模块

        mainBt1.setOnClickListener(this);
        mainBt2.setOnClickListener(this);
        mainBt3.setOnClickListener(this);


        radioGroup.setOnCheckedChangeListener(this);
    }

    private void initView() {
        pre = findViewById(R.id.pre);
        mainBt1 = findViewById(R.id.main_bt1);
        mainBt2 = findViewById(R.id.main_bt2);
        mainBt3 = findViewById(R.id.main_bt3);
        radioGroup = findViewById(R.id.radioGroup);

        textGaoDe = findViewById(R.id.gaodeInfo);
        seekBar = findViewById(R.id.seekBar);




    }





    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_bt1:

                break;

            case R.id.main_bt2:

                if (gaoDeTools.isStart()) {


                    gaoDeTools.stop();
                    mainBt2.setText("开始录像");
                    mainBt3.setText("继续");
                    mainBt3.setVisibility(View.GONE);
                    textGaoDe.setText("点击开始录像");

                    cameraX.stopVideo();



                } else {
                    gaoDeTools.start();
                    mainBt2.setText("保存录像");
                    mainBt3.setText("暂停");
                    mainBt3.setVisibility(View.VISIBLE);

                    cameraX.startVideo();

                }
                break;

            case R.id.main_bt3:

                break;
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){

            case R.id.radioButton1:
                mainBt1.setVisibility(View.VISIBLE);
                break;
            case R.id.radioButton2:
                mainBt1.setVisibility(View.GONE);
                //按经纬分段
                break;
            case R.id.radioButton3:
                mainBt1.setVisibility(View.GONE);
                //按时间分段
                break;
        }

    }


    @Override
    public void onGranted() {
        //开始预览
        cameraX = new CameraX(this, pre);
        cameraX.setSeekBar(seekBar);
        //初始高德定位
        gaoDeTools = GaoDe.getGaoDeTools(this, this);
    }

    @Override
    public void onDenied() {
        showToast("权限不足");
        //finish();
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



}