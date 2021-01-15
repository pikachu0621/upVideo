/**
 * 项目的添加
 */

package com.pikachu.upvideo.init;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.tools.AppInfo;
import com.pikachu.upvideo.tools.Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddProjects {


    @SuppressLint("StaticFieldLeak")
    private static AddProjects addProjects;
    private final Activity activity;
    private final String videoPath;

    public static AddProjects getAddProject(Activity activity) {
        if (addProjects == null)
            addProjects = new AddProjects(activity);
        return addProjects;
    }

    public AddProjects(Activity activity) {
        this.activity = activity;
        videoPath = Tools.getVideoPath(activity);

    }


    //判断是否存在改项目
    private boolean isProjectName(String name) {

        String[] list1 = new File(videoPath).list();
        if (list1==null || list1.length <= 0 ) return false;
        for (String l: list1) {
            return l.equals(name);
        }
        return false;
    }


    //添加项目
    public void addProject() {


        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(activity).inflate(R.layout.ui_edialog, null);
        EditText edit = inflate.findViewById(R.id.ui_ed_edit);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setIcon(R.drawable.ic_add_project)
                .setTitle(R.string.app_add_project)
                .setView(inflate)
                .setPositiveButton(R.string.app_project_add, (dialog, which) -> {
                    String s = edit.getText().toString();
                    if (s.equals("")) Tools.tw(activity,"请输入项目名");
                    else if (isProjectName(s)) Tools.tw(activity,"本地已存在该项目，换个名称吧");
                    else if (addProject(s)) Tools.tw(activity,"创建成功");
                    else  Tools.tw(activity,"创建失败");
                })
                .setNegativeButton(R.string.app_project_cancel, null);
        builder.show();
    }



    //添加项目
    private boolean addProject(String name) {
        File file = new File(videoPath + name );
        if (!file.exists())
            if (!file.mkdir())
                return false;
        VideoUpJson videoUpJson = new VideoUpJson();
        String r = new Gson().toJson(videoUpJson);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(videoPath + name + "/" + AppInfo.videoProjectName));
            out.write(r);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }







    //删除一个项目
    public boolean deleteProject(String projectName) {

        return false;
    }





    //修改一个项目 (null 不修改保持原来的)
    public boolean modifyProject(String projectName, String projectTime, String projectAddress, int projectPoint, int projectType) {

        return false;
    }

    //修改一个项目
    public boolean modifyProject(VideoUpJson videoUpJson) {

        return false;
    }


    //读取全部项目
    public List<VideoUpJson> readProject() {

        String[] list1 = new File(videoPath).list((dir, name) -> {
            if (dir.isDirectory() && new File( videoPath + name + "/" + AppInfo.videoProjectName ).exists())
                return true;
            return false;
        });

        List<VideoUpJson> videoUpJson = new ArrayList<>();
        if (list1!=null && list1.length>0){
            for (String s: list1) {
                String s1 = readFile(videoPath + s + "/" + AppInfo.videoProjectName );
                videoUpJson.add( new Gson().fromJson(s1, VideoUpJson.class));
            }
        }
        return videoUpJson;
    }




    //读取文件
    private String readFile(String path){

        File file = new File(path );
        StringBuilder stringBuffer = new StringBuilder();
        String oneLine ;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ( (oneLine = bufferedReader.readLine() )!= null){
                stringBuffer.append(oneLine);
            }
            fileInputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }


}
