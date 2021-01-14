package com.pikachu.upvideo.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Tools {


    private static Toast toast;

    @SuppressLint("ShowToast")
    public static Toast tw(Context context, String msg) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    //获取路径
    public static String getVideoPath(Context context){
        return context/*.getFilesDir()*/.getExternalFilesDir("").getAbsolutePath() + AppInfo.videoPath;
    }

}
