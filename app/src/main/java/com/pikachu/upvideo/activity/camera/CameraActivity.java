package com.pikachu.upvideo.activity.camera;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.util.Log;
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
import com.pikachu.upvideo.activity.list.ListActivity;
import com.pikachu.upvideo.cls.CameraStartData;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.util.AppInfo;
import com.pikachu.upvideo.util.base.BaseActivity;
import com.pikachu.upvideo.util.state.PKStatusBarTools;
import com.pikachu.upvideo.util.tools.ToolAddProjects;
import com.pikachu.upvideo.util.tools.ToolCameraX;
import com.pikachu.upvideo.util.tools.ToolGaoDe;
import com.pikachu.upvideo.util.tools.ToolOther;
import com.pikachu.upvideo.util.tools.ToolTimer;
import com.pikachu.upvideo.util.view.CameraBtView;
import com.pikachu.upvideo.util.view.QMUIRadiusImageView;

import java.io.File;
import java.util.List;

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
    private boolean isClickMin;
    private VideoUpJson videoUpJson;
    private CameraStartData cameraStartData;
    private ToolAddProjects addProject;
    private VideoUpJson.SonProject sonProject;
    private String sonPath;
    private VideoUpJson.SonProject.ListHisVideo listHisVideo;
    private AMapLocation aMapLocation;
    private String videoNameTime;


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
        cameraStartData = getSerializableExtra(AppInfo.START_ACTIVITY_KEY_2, CameraStartData.class);

        videoUpJson = cameraStartData.getVideoUpJson();
        sonProject = cameraStartData.getSonProject();
        sonPath = cameraStartData.getSonPath();
        listHisVideo = cameraStartData.getListHisVideo();


        //添加bar
        //setSupportActionBar(barToolbar);
        //setHead(true,videoUpJson.getProjectName() ,null, this::finish);
        //开始预览
        cameraX = new ToolCameraX(this, caPre, this, cameraStartData.getSonPath());
        //cameraX.setSeekBar(seekBar); //设置变焦bar
        //初始高德定位
        toolGaoDeTools = ToolGaoDe.getGaoDeTools(this, this); //可删除


        //初始计时器
        timer = ToolTimer.getTimer(this, this);
        //初始添加项目
        addProject = ToolAddProjects.getAddProject(this);
        //手动分段显示按钮
        caCameraBt2.setVisibility(videoUpJson.getProjectMode() == 1 && cameraStartData.getStartType() != 2 ? View.VISIBLE : View.GONE);

        caCameraBt1.setOnClickListener((view, isSed) -> {
            if (isSed) {
                videoNameTime = getVideoName();

                startVideo(sonProject.getSonProjectName(), videoNameTime);
            } else
                stopVideo();
            isClickMin = false;
            dStop = false; // 用于弹框退出
            return true;
        });

        caCameraBt2.setOnClickListener((view, isSed) -> {
            minVideo();
            return true;
        });

    }


    ////////////////////////////////// 录像相关 ////////////////////////////////////////////////////
    private String getVideoName() {
        if (cameraStartData.getStartType() == 2)
            return listHisVideo.getVideoName();
        return ToolOther.getTime();
    }

    // 开始录像
    private void startVideo(String nodeName, String videoName) {

        timer.run(); // 开始计时
        toolGaoDeTools.start(); // 开始定位
        cameraX.startVideo(/*sonProject.getSonProjectName()*/nodeName,
                /*getVideoName()*/videoName + ".mp4"); // 开始录像
        caLi1.setVisibility(View.VISIBLE);
        showToast("开始录像");
    }

    //结束录像
    private void stopVideo() {

        timer.afresh();// 结束计时
        toolGaoDeTools.stop(); // 结束定位
        cameraX.stopVideo(); // 结束录像/并保存
        caLi1.setVisibility(View.GONE);
        showToast("结束录像");
    }


    //分段
    private void minVideo() {

        if (caCameraBt1.isSed()) {
            isClickMin = true;
            //结束录制
            stopVideo();
            timer.afresh();
            showToast("分段中");

        } else
            showToast("还没有开始录像");
        //showToast("分段");


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
    public PKStatusBarTools pkStatusBarTools() {
        PKStatusBarTools pkStatusBarTools = PKStatusBarTools.with(this).hideSTS().hideNON();
        pkStatusBarTools.init();
        return pkStatusBarTools;
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
        this.aMapLocation = aMapLocation;
        StringBuilder stringBuffer = new StringBuilder();
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            stringBuffer.append(VideoUpJson.aMapLocation2MapInfo(aMapLocation).toString());
        } else
            stringBuffer.append("定位失败,请打开GPS定位");
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
            } else
                finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    //更新数据
    private boolean upData() {

        if (aMapLocation == null)
            return false;

        List<VideoUpJson.SonProject> listSon = this.videoUpJson.getListSon();
        int i = listSon.indexOf(this.sonProject);
        VideoUpJson.SonProject sonProject = listSon.get(i);
        List<VideoUpJson.SonProject.ListHisVideo> sonProjectVideo = sonProject.getSonProjectVideo();

        VideoUpJson.SonProject.MapInfo mapInfo = null;
        if (aMapLocation != null)
            mapInfo = VideoUpJson.aMapLocation2MapInfo(aMapLocation);

        VideoUpJson.SonProject.ListHisVideo listHisVideo = new VideoUpJson.SonProject.ListHisVideo();
        if (mapInfo != null)
            listHisVideo.setVideoMapInfo(mapInfo);
        //Log.i("test_t",videoNameTime +"   mmp");
        listHisVideo.setVideoName(videoNameTime);
        listHisVideo.setVideoTime(ToolOther.getTime());
        listHisVideo.setVideoType(0);
        listHisVideo.setVideoPx(0);
        listHisVideo.setVideoWh(0);

        if (cameraStartData.getStartType() == 1) {
            //add video
            sonProjectVideo.add(0, listHisVideo);
        } else {
            //re Video
            addProject.deleteFile(videoUpJson.getProjectName(),
                    sonProject.getSonProjectName(),
                    cameraStartData.getListHisVideo().getVideoName());
            listHisVideo.setVideoTime(cameraStartData.getListHisVideo().getVideoTime());
            int indexOf = sonProjectVideo.indexOf(cameraStartData.getListHisVideo());
            sonProjectVideo.set(indexOf, listHisVideo);
        }


        sonProject.setSonProjectVideo(sonProjectVideo);
        if (mapInfo != null)
            sonProject.setSonProjectEndMapInfo(mapInfo);

        listSon.set(i, sonProject);

        return addProject.writeJson(this.videoUpJson);
    }


    //视频保存成功
    @Override
    public void onVideoSaved(@NonNull File file, String filePath) {
        //showToast("已保存到： " + filePath);
        //更新数据
        if (!upData()) {
            showToast("数据更新失败");
            return;
        }

        if (isClickMin) {

            addProject.addSonProject(this.videoUpJson, aMapLocation, ToolOther.getTime(), (videoUpJson, sonProject, sonPath) -> {
                this.videoUpJson = videoUpJson;
                this.sonProject = sonProject;
                this.sonPath = sonPath;
                //开始录像
                startVideo(sonProject.getSonProjectName(),videoNameTime = getVideoName());

            });

        }

        if (!isClickMin) {
            showToast("添加完成");
            finish();
            if (cameraStartData.isIndex()) {
                Intent intent = new Intent();
                intent.setClass(this, ListActivity.class);
                intent.putExtra(AppInfo.START_ACTIVITY_KEY_1, videoUpJson);
                startActivity(intent);
            }
        }

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