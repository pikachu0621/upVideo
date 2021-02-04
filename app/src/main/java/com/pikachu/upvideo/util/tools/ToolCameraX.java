package com.pikachu.upvideo.util.tools;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.Dialog;
import android.util.Log;
import android.view.Surface;
import android.widget.SeekBar;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ToolCameraX {


    @SuppressLint("StaticFieldLeak")
    private static ToolCameraX cameraX;
    private final OnVideoSavedCallback onVideoSavedCallback;
    private final String path;
    private Activity activity;
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> instance;
    private ProcessCameraProvider processCameraProvider;
    private Preview preview;
    private ImageCapture mImageCapture;
    private VideoCapture videoBuild;
    private CameraSelector cameraId = CameraSelector.DEFAULT_BACK_CAMERA; //使用后置摄像头
    private Camera camera;
    private SeekBar seekBar;
    private Dialog progressDialog;


    public interface OnVideoSavedCallback {
        void onVideoSaved(@NonNull File file, String filePath);

        void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause, String filePath);
    }


    public static ToolCameraX getCameraX(Activity activity, @IdRes int id, OnVideoSavedCallback onVideoSavedCallback
            , String path) {
        return getCameraX(activity, activity.findViewById(id), onVideoSavedCallback, path);
    }

    public static ToolCameraX getCameraX(Activity activity, PreviewView previewView, OnVideoSavedCallback onVideoSavedCallback,
                                         String path) {
       /* if (cameraX == null)
            cameraX = new ToolCameraX(activity, previewView, onVideoSavedCallback);*/
        return /*cameraX */new ToolCameraX(activity, previewView, onVideoSavedCallback, path);
    }

    public ToolCameraX(Activity activity, PreviewView previewView, OnVideoSavedCallback onVideoSavedCallback,
                       String path) {
        this.activity = activity;
        this.previewView = previewView;
        this.onVideoSavedCallback = onVideoSavedCallback;
        this.path = path;
        ToolAddProjects.createFiles(path);
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
                    camera = processCameraProvider.bindToLifecycle((LifecycleOwner) activity,
                            cameraId, preview, videoBuild, mImageCapture);
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
    public void startVideo(String nodeName,String videoName) {
        saveVideo(nodeName+"/"+videoName);
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
        if (videoBuild != null)
            videoBuild.stopRecording(); //停止录制
    }

    //分段
    public void partVideo() {
        //saveVideo();
    }


    //保存视频
    @SuppressLint("RestrictedApi")
    private void saveVideo(String path) {


       /* File file = new File(name);
        if (!file.exists()) file.mkdirs();*/
        String finalS = this.path + /*ToolOther.getTime()*/ path;
        File file = new File(finalS);

        progressDialog = ToolOther.showDialog(activity, "Loading...", "视频处理中...");
        videoBuild.startRecording(file, Executors.newSingleThreadExecutor(),
                new VideoCapture.OnVideoSavedCallback() {
                    @Override
                    public void onVideoSaved(@NonNull File file) {
                        progressDialog.dismiss();
                        //保存视频成功回调，会在停止录制时被调用
                        activity.runOnUiThread(() -> onVideoSavedCallback.onVideoSaved(file, finalS));
                        //       ToolOther.tw(activity, "已保存到： " + finalS)

                    }

                    @Override
                    public void onError(int videoCaptureError, @NonNull String message,
                                        @Nullable Throwable cause) {
                        progressDialog.dismiss();
                        //保存失败的回调，可能在开始或结束录制时被调用8
                        Log.e("test_t", "onError: " + message);
                        activity.runOnUiThread(() -> onVideoSavedCallback.onError(videoCaptureError, message, cause, finalS));
                        //ToolOther.tw(activity, "保存失败： " + message)
                    }
                });


    }
}
