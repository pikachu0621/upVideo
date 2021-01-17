/**
 * 项目的添加
 */

package com.pikachu.upvideo.init;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.pikachu.upvideo.R;
import com.pikachu.upvideo.activity.index.ProjectFragment;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.tools.AppInfo;
import com.pikachu.upvideo.tools.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AddProjects {


    @SuppressLint("StaticFieldLeak")
    private static AddProjects addProjects;
    private final Activity activity;
    private final String videoPath;
    private GaoDe gaoDe;
    private int gaoDeP = 0; //已定位次数
    private boolean gaoDeStop; //是否已暂停
    private EditText edit;
    private EditText edit2;
    @SuppressLint("InflateParams")
    private View inflate;
    private Spinner addSpinner1;
    private Spinner addSpinner2;
    private Spinner addSpinner3;
    private AlertDialog.Builder builder;
    private String name;
    private int selectedItemPosition;
    private int selectedItemPosition1;
    private int selectedItemPosition2;
    private String sEdit2 = "0";
    private long integer;


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
        if (list1 == null || list1.length <= 0) return false;
        for (String l : list1) {
            if (l.equals(name) &&
                    new File(videoPath + l + "/" + AppInfo.videoProjectName).exists())
                return true;
        }
        return false;
    }


    //初始窗
    private void initAddDialog() {

        inflate = LayoutInflater.from(activity).inflate(R.layout.ui_edialog, null);
        edit = inflate.findViewById(R.id.ui_ed_edit);
        edit2 = inflate.findViewById(R.id.ui_ed_edit2);
        addSpinner1 = inflate.findViewById(R.id.add_spinner1);
        addSpinner2 = inflate.findViewById(R.id.add_spinner2);
        addSpinner3 = inflate.findViewById(R.id.add_spinner3);


        addSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int hi;
                if (position == 3) hi = R.string.app_fd_t;
                else hi = R.string.app_fd_m;
                edit2.setVisibility(position == 2 || position == 3 ? View.VISIBLE : View.GONE);
                edit2.setHint(hi);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    //添加主项目
    public void addProject(View progressBar, View floatingActionButton,
                           ProjectFragment projectFragment) {

        initAddDialog();
        builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.app_add_project);
        builder.setView(inflate);
        builder.setPositiveButton(R.string.app_project_add, (dialog, which) -> {

            //重置
            gaoDeStop = false;
            gaoDeP = 0;
            name = edit.getText().toString();

            selectedItemPosition = addSpinner1.getSelectedItemPosition();
            selectedItemPosition1 = addSpinner2.getSelectedItemPosition();
            selectedItemPosition2 = addSpinner3.getSelectedItemPosition();


            if (edit2.getVisibility() == View.VISIBLE) {
                sEdit2 = edit2.getText().toString();
                if (sEdit2 == null || sEdit2.equals("")) {
                    Tools.tw(activity, "创建失败,请输入距离/时间");
                    return;
                }
            }
            integer = Integer.parseInt(sEdit2);


            if (name.equals("")) Tools.tw(activity, "请输入项目名");
            else if (isProjectName(name)) Tools.tw(activity, "本地已存在该项目，换个名称吧");
            else {

                File file = new File(videoPath + name);
                if (!file.exists()) if (!file.mkdirs()) {
                    Tools.tw(activity, "创建失败");
                    return;
                }

                floatingActionButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                gaoDe = GaoDe.getGaoDeTools(activity, aMapLocation -> {
                    //暂停防止多余的回调
                    if (gaoDeStop) return;
                    //本次是否成功
                    boolean isC = false;
                    StringBuilder stringBuffer = new StringBuilder();
                    //判断定位成功
                    if (aMapLocation != null && aMapLocation.getErrorCode()
                            == 0 && !aMapLocation.getAddress().equals("")) {
                        stringBuffer.append(aMapLocation.getAddress());
                        isC = true;
                    } else {
                        stringBuffer.append("定位失败");
                        gaoDeP++;
                    }

                    //失败重试
                    if (isC) {
                        gaoDe.stop();
                        gaoDeStop = true;
                    } else if (gaoDeP < 3) return;
                    else {
                        gaoDe.stop();
                        gaoDeStop = true;
                    }

                    //构成 class
                    VideoUpJson videoUpJson = new VideoUpJson(name, Tools.getTime(),
                            stringBuffer.toString(), 0,
                            selectedItemPosition, integer,
                            selectedItemPosition1, selectedItemPosition2,
                            new ArrayList<>());
                    //Gson 转 String
                    String r = new Gson().toJson(videoUpJson);
                    try {


                        Writer out = new FileWriter(
                                new File(videoPath + name + "/" + AppInfo.videoProjectName)
                        );
                        out.write(r);
                        out.close();

                       /* BufferedWriter out = new BufferedWriter(new FileWriter(videoPath + name + "/" + AppInfo.videoProjectName));
                        out.write(r);
                        out.close();*/
                        Tools.tw(activity, "创建成功");
                        //显示按钮
                        floatingActionButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        //刷新数据
                        projectFragment.refresh();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Tools.tw(activity, "创建失败");
                    }
                    //显示按钮
                    floatingActionButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                });

                //开始定位
                gaoDe.start();

            }
        });
        builder.setNegativeButton(R.string.app_project_cancel, null);
        /*.setIcon(R.drawable.ic_add_project)*/

        builder.show();
    }


    //删除一个主项目
    public void deleteProject(String projectName) {
        deleteFile(new File(videoPath +projectName));
    }


    //修改一个主项目 (null 不修改保持原来的)
    public boolean modifyProject(String projectName, String projectTime,
                                 String projectAddress, int projectPoint,
                                 int projectType) {

        return false;
    }

    //修改一个主项目
    public boolean modifyProject(VideoUpJson videoUpJson) {

        return false;
    }


    //读取全部主项目
    public List<VideoUpJson> readProject() {

        String[] list1 = new File(videoPath).list((dir, name) -> {
            if (dir.isDirectory() && new File(videoPath + name + "/" + AppInfo.videoProjectName).exists())
                return true;
            return false;
        });
        List<VideoUpJson> videoUpJson = new ArrayList<>();
        if (list1 != null && list1.length > 0) {
            for (String s : list1) {
                String s1 = readFile(videoPath + s + "/" + AppInfo.videoProjectName);
                videoUpJson.add(new Gson().fromJson(s1, VideoUpJson.class));
            }
        }

        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat(AppInfo.timeStr);
        Collections.sort(videoUpJson, (o1, o2) -> {
            try {
                Date dt1 = df.parse(o1.getProjectTime());
                Date dt2 = df.parse(o2.getProjectTime());
                if (dt1.getTime() > dt2.getTime())
                    return -1;  // dt1 在dt2前
                if (dt1.getTime() < dt2.getTime())
                    return 1; // dt1在dt2后
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
        return videoUpJson;
    }







    //读取文件
    private String readFile(String path) {

        File file = new File(path);
        StringBuilder stringBuffer = new StringBuilder();
        String oneLine;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ((oneLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(oneLine);
            }
            fileInputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }


    //删除文件夹
    private void deleteFile(File file) {
        if (file == null || !file.exists()) return;
        File[] files = file.listFiles();
        for (File f : files)
            if (f.isDirectory()) deleteFile(f);
            else f.delete();
        file.delete();
    }


}
