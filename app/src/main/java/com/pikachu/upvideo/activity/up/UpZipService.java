package com.pikachu.upvideo.activity.up;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.iceteck.silicompressorr.SiliCompressor;

import java.net.URISyntaxException;

public class UpZipService extends Service {
    public UpZipService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ConnectBinder();
    }


    class ConnectBinder extends Binder{
        public String startZipVideo(String videoPath,String destinationDirectory){
            return UpZipService.this.startZipVideo(videoPath,destinationDirectory);
        }

        public UpZipService getUpZipService(){
            return UpZipService.this;
        }
    }






    public String startZipVideo(String videoPath,String destinationDirectory){
        try {
            return SiliCompressor.with(this).compressVideo(videoPath, destinationDirectory);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }




















}