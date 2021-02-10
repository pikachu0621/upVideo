package com.pikachu.upvideo.util.tools;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pikachu.upvideo.R;
import com.pikachu.upvideo.util.AppInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ToolOther {


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
        return context/*.getFilesDir()*/.getExternalFilesDir("")
                .getAbsolutePath() + AppInfo.videoPath;
    }


    @SuppressLint("SimpleDateFormat")
    public static String getTime(String pattern, long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        if (time == 0)
            return simpleDateFormat.format(new Date());
        return simpleDateFormat.format(time);
    }

    public static String getTime(String pattern){
        return getTime(pattern,0);
    }


    public static String getTime(){
        return getTime(AppInfo.timeStr);
    }

    public static String getTime(long time){
        return getTime(AppInfo.timeStr,time);
    }


    /**
     * dp  转  px
     *
     * @param dpValue px值
     * @param context 上下文
     */
    public static int dp2px(Context context, int dpValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics()) + 0.5F);
    }



    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }


    /**
     * 设置一个占位view 用于占位状态栏
     *
     * @param context
     * @param view
     */
    public static void setNonHigh(Context context, View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = getStatusBarHeight(context);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置一个占位view 用于占位状态栏
     *
     * @param context
     * @param view
     */
    public static void setNonHigh(Context context, View view,int attachValue) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = getStatusBarHeight(context)+attachValue;
        view.setLayoutParams(layoutParams);
    }


    public static Dialog showDialog(Context context, String title, String msg){
        ProgressDialog progressDialog=new ProgressDialog(context);
        if (title != null && !title.equals(""))
            progressDialog.setTitle(title);
        if (msg != null && !msg.equals(""))
            progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }


    /**
     * 鸭缩文件夹
     * @param srcFileString 文件夹路径
     * @param zipFileString 鸭缩后文件路径
     */
    public static void zipFolder(String srcFileString, String zipFileString) {
        //创建ZIP
        try {
            ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(new File(zipFileString + "" + System.currentTimeMillis() + ".zip")));
            //创建文件
            File file = new File(srcFileString);
            //压缩
            zipFiles(file.getParent() + File.separator, file.getName(), outZip);
            //完成和关闭
            outZip.finish();
            outZip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void zipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) {
        try {
            if (zipOutputSteam == null)
                return;
            File file = new File(folderString + fileString);
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(fileString);
                FileInputStream inputStream = new FileInputStream(file);
                zipOutputSteam.putNextEntry(zipEntry);
                int len;
                byte[] buffer = new byte[4096];
                while ((len = inputStream.read(buffer)) != -1) {
                    zipOutputSteam.write(buffer, 0, len);
                }
                zipOutputSteam.closeEntry();
            } else {
                //文件夹
                String fileList[] = file.list();
                //没有子文件和压缩
                if (fileList.length <= 0) {
                    ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                    zipOutputSteam.putNextEntry(zipEntry);
                    zipOutputSteam.closeEntry();
                }
                //子文件和递归
                for (int i = 0; i < fileList.length; i++) {
                    zipFiles(folderString + fileString + "/", fileList[i], zipOutputSteam);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









}
