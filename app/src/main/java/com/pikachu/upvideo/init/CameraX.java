package com.pikachu.upvideo.init;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.widget.SeekBar;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.pikachu.upvideo.tools.Tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CameraX {


    @SuppressLint("StaticFieldLeak")
    private static CameraX cameraX;
    private final Activity activity;
    private final PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> instance;
    private ProcessCameraProvider processCameraProvider;
    private Preview preview;
    private ImageCapture mImageCapture;
    private VideoCapture videoBuild;
    private final CameraSelector cameraId = CameraSelector.DEFAULT_BACK_CAMERA; //使用后置摄像头
    private Camera camera;
    private SeekBar seekBar;


    public static CameraX getCameraX(Activity activity, @IdRes int id) {
        return getCameraX(activity, activity.findViewById(id));
    }

    public static CameraX getCameraX(Activity activity, PreviewView previewView) {
        if (cameraX == null)
            cameraX = new CameraX(activity, previewView);
        return cameraX;
    }

    public CameraX(Activity activity, PreviewView previewView) {
        this.activity = activity;
        this.previewView = previewView;
        //开始预览
        startCamera();
    }


    //开始预览
    @SuppressLint("RestrictedApi")
    private void startCamera() {

        instance = ProcessCameraProvider.getInstance(activity);
        instance.addListener(() -> {


            try {
                processCameraProvider = instance.get();//获取相机信息


                //      预览配置
                preview = new Preview.Builder()
                        .setTargetAspectRatio(AspectRatio.RATIO_16_9)  //设置宽高比
                        .setTargetRotation(Surface.ROTATION_0) // 设置旋转角度
                        .build();
                //       拍照配置
                mImageCapture = new ImageCapture.Builder()
                        .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                        .build();

                //        录像配置
                videoBuild = new VideoCapture.Builder()//录像用例配置
                        .setTargetAspectRatio(AspectRatio.RATIO_16_9) //设置高宽比
                        /*.setTargetRotation(viewFinder.display.rotation)//设置旋转角度
                        .setAudioRecordSource(MediaRecorder.AudioSource.MIC)//设置音频源麦克风*/
                        .build();



                if (processCameraProvider != null) {
                    processCameraProvider.unbindAll(); //解绑所有
                    camera = processCameraProvider.bindToLifecycle((LifecycleOwner) activity, cameraId, preview, videoBuild, mImageCapture);
                }
                preview.setSurfaceProvider(previewView.createSurfaceProvider());

                if (seekBar != null)
                    setSeekBarMax();
            } catch (ExecutionException | InterruptedException e) {
                Log.e("test_t", "error:" + e);
            }
        }, ContextCompat.getMainExecutor(activity));

    }

    //获取相机最大变焦
    private float getMaxZoom() {
        if (camera == null)
            return 0;
        return camera.getCameraInfo().getZoomState().getValue().getMaxZoomRatio();
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }


    private void setSeekBarMax() {
        seekBar.setMax((int) getMaxZoom());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setZoom(progress / getMaxZoom());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    //设置缩放
    private void setZoom(float zoom) {
        camera.getCameraControl().setLinearZoom(zoom);
    }


    //开始录像 （重新开始）
    public void startVideo() {
        saveVideo();
    }

    //暂停录像 （暂停录像）
    public void pauseVideo() {

    }

    //继续录像 （接着录像）
    public void carryOnVideo() {

    }

    //停止录像（停止录像）
    @SuppressLint("RestrictedApi")
    public void stopVideo() {
        videoBuild.stopRecording(); //停止录制
    }

    //分段
    public void partVideo() {
        //saveVideo();
    }


    //保存视频
    @SuppressLint("RestrictedApi")
    private void saveVideo() {

        String s = Tools.getVideoPath(activity);
        File file = new File(s);
        if(!file.exists()) file.mkdir();
        file = new File( s = s + new SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss", Locale.CHINA
        ).format(System.currentTimeMillis()) + ".mp4");

        String finalS = s;
        videoBuild.startRecording(file,Executors.newSingleThreadExecutor() , new VideoCapture.OnVideoSavedCallback() {
            @Override
            public void onVideoSaved(@NonNull File file) {
                //保存视频成功回调，会在停止录制时被调用
                activity.runOnUiThread(() ->  Tools.tw(activity, "已保存到： " + finalS) );
            }

            @Override
            public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                //保存失败的回调，可能在开始或结束录制时被调用8
                Log.e("test_t", "onError: " + message);

                activity.runOnUiThread(() -> Tools.tw(activity, "保存失败： " + message));

            }
        });


    }
}
