/**
 * 项目的添加
 */

package com.pikachu.upvideo.util.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.pikachu.upvideo.R;
import com.pikachu.upvideo.activity.index.ProjectFragment;
import com.pikachu.upvideo.activity.list.RecyclerAdapter;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.util.AppInfo;
import com.pikachu.upvideo.util.view.PkEditText;

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

public class ToolAddProjects {


    @SuppressLint("StaticFieldLeak")
    private static ToolAddProjects toolAddProjects;
    private final Activity activity;
    private final String videoPath;
    private ToolGaoDe toolGaoDe;
    private int gaoDeP = 0; //已定位次数
    private boolean gaoDeStop; //是否已暂停
    private PkEditText edit;
    private PkEditText edit2;
    @SuppressLint("InflateParams")
    private View inflate;
    private Spinner addSpinner1;
    private Spinner addSpinner2;
    private Spinner addSpinner3;
    private AlertDialog.Builder builder;
    private String name = "", msg = "";
    private int selectedItemPosition;
    private int selectedItemPosition1;
    private int selectedItemPosition2;
    private String sEdit2 = "0";
    private long integer;
    private Dialog progressDialog;


    public static ToolAddProjects getAddProject(Activity activity) {
       /* if (toolAddProjects == null)
            toolAddProjects =new ToolAddProjects(activity);*/
        return /*toolAddProjects*/new ToolAddProjects(activity);
    }

    public ToolAddProjects(Activity activity) {
        this.activity = activity;
        videoPath = ToolOther.getVideoPath(activity);

    }

    /////////////////////////////////////////接口////////////////////////////////////////////////////

    public interface OnChangeCompleteListener {
        void changeComplete(VideoUpJson videoUpJson, VideoUpJson.SonProject sonProject, String sonPath);
    }

    public interface OnMapListener {
        //返回true 直接停止本次
        boolean mapError(String msg);

        void mapComplete(AMapLocation aMapLocation, String errorMsg);

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////主项目///////////////////////////////////////////////////////

    //初始窗
    @SuppressLint("InflateParams")
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

            name = edit.getText().toString();

            selectedItemPosition = addSpinner1.getSelectedItemPosition();
            selectedItemPosition1 = addSpinner2.getSelectedItemPosition();
            selectedItemPosition2 = addSpinner3.getSelectedItemPosition();


            if (edit2.getVisibility() == View.VISIBLE) {
                sEdit2 = edit2.getText().toString();
                if (sEdit2 == null || sEdit2.equals("")) {
                    ToolOther.tw(activity, "创建失败,请输入距离/时间");
                    return;
                }
            }
            integer = Integer.parseInt(sEdit2);

            if (name.equals("")) ToolOther.tw(activity, "请输入项目名");
            else if (isProjectName(name)) ToolOther.tw(activity, "本地已存在该项目，换个名称吧");
            else {

                //创建文件夹
                if (!createFiles(videoPath + name)) {
                    ToolOther.tw(activity, "创建失败");
                    return;
                }

                floatingActionButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                mapOnce(new OnMapListener() {

                    @Override
                    public boolean mapError(String msg) {
                        return false;
                    }

                    @Override
                    public void mapComplete(AMapLocation aMapLocation, String errorMsg) {

                        //构成 class
                        VideoUpJson videoUpJson = new VideoUpJson(name, ToolOther.getTime(),
                                errorMsg, 0,
                                selectedItemPosition, integer,
                                selectedItemPosition1, selectedItemPosition2,
                                new ArrayList<>());

                        if (writeJson(videoUpJson, videoPath + name + "/" + AppInfo.videoProjectName)) {
                            //显示按钮
                            ToolOther.tw(activity, "创建成功");
                            floatingActionButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            //刷新数据
                            projectFragment.refresh();
                        } else {
                            ToolOther.tw(activity, "创建失败");
                        }
                        floatingActionButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }

        });
        builder.setNegativeButton(R.string.app_project_cancel, null);
        /*.setIcon(R.drawable.ic_add_project)*/

        builder.show();
    }


    //删除一个主项目
    public void deleteProject(String projectName) {
        deleteFiles(new File(videoPath + projectName));
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

    //////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////子项目////////////////////////////////////////////////

    /**
     * 添加节点
     *
     * @param rea                      用于刷新列表
     * @param videoUpJson              videoUpJson 用于写入数据
     * @param onChangeCompleteListener 改变完成
     * @param isShowEdit               是否显示编辑框
     */
    public void addSonProject(RecyclerAdapter rea, VideoUpJson videoUpJson,
                              OnChangeCompleteListener onChangeCompleteListener, boolean isShowEdit) {
        if (!isShowEdit) {
            addSonProject(videoUpJson, rea, onChangeCompleteListener, ToolOther.getTime(), "暂无备注");
            return;
        }

        inflate = LayoutInflater.from(activity).inflate(R.layout.ui_son_add_project, null);
        edit = inflate.findViewById(R.id.ui_son_name);
        edit2 = inflate.findViewById(R.id.ui_son_msg);


        builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.app_son_add_project)
                .setMessage(R.string.app_son_add_project_msg);
        builder.setView(inflate);
        builder.setPositiveButton(R.string.app_project_add, (dialog, which) -> {

            name = edit.getText().toString();
            msg = edit2.getText().toString();

            if (name.equals(""))
                name = ToolOther.getTime(); //没有输入名 则用时间代替
            if (msg.equals(""))
                msg = "暂无备注";

            for (VideoUpJson.SonProject sonProject : videoUpJson.getListSon()) {
                if (sonProject.getSonProjectName().equals(name)) {
                    ToolOther.tw(activity, "节点已存在，换个名称吧");
                    return;
                }
            }

            addSonProject(videoUpJson, rea, onChangeCompleteListener, name, msg);


        });
        builder.setNegativeButton(R.string.app_project_cancel, null);
        //builder.setCancelable(false);
        builder.show();
    }

    /**
     * 公用模块
     *
     * @param videoUpJson
     * @param rea
     * @param onChangeCompleteListener
     * @param name
     * @param msg
     */
    private void addSonProject(VideoUpJson videoUpJson, RecyclerAdapter rea,
                               OnChangeCompleteListener onChangeCompleteListener,
                               String name, String msg) {


        progressDialog = ToolOther.showDialog(activity, "Loading...", "创建节点中...");

        mapOnce(new OnMapListener() {
            @Override
            public boolean mapError(String msg) {
                ToolOther.tw(activity, "定位失败，请确保手机开启了GPS");
                progressDialog.dismiss();
                return true;
            }

            @Override
            public void mapComplete(AMapLocation aMapLocation, String errorMsg) {
                addSonProject(videoUpJson, rea, onChangeCompleteListener, aMapLocation, name, msg);
            }
        });
    }


    /**
     * 直接添加
     *
     * @param videoUpJson
     * @param onChangeCompleteListener
     */
    public void addSonProject(VideoUpJson videoUpJson, OnChangeCompleteListener onChangeCompleteListener) {
        addSonProject(null, videoUpJson, onChangeCompleteListener, false);
    }

    /**
     * 有地址数据
     *
     * @param videoUpJson
     * @param rea                      可空
     * @param onChangeCompleteListener 监听
     * @param aMapLocation             定位数据
     * @param name                     标题
     * @param msg                      备注
     */
    private void addSonProject(VideoUpJson videoUpJson,
                              RecyclerAdapter rea,
                              OnChangeCompleteListener onChangeCompleteListener,
                              AMapLocation aMapLocation,
                              String name,
                              String msg) {

        //创建文件夹
        String sonPath = videoPath + videoUpJson.getProjectName() + "/";

        if (!createFiles(sonPath + name)) {
            ToolOther.tw(activity, "创建失败");
            if (progressDialog != null)
                progressDialog.dismiss();
            return;
        }

        VideoUpJson.SonProject sonProject = new VideoUpJson.SonProject();
        sonProject.setSonProjectName(name);
        sonProject.setSonProjectMsg(msg);
        sonProject.setSonProjectStartMapInfo(VideoUpJson.aMapLocation2MapInfo(aMapLocation));
        sonProject.setSonProjectEndMapInfo(VideoUpJson.aMapLocation2MapInfo(aMapLocation));
        sonProject.setSonProjectVideo(new ArrayList<>());
        List<VideoUpJson.SonProject> listSon = videoUpJson.getListSon();
        listSon.add(/*listSon.size()*/0, sonProject);
        videoUpJson.setListSon(listSon);
        if (writeJson(videoUpJson)) {
            ToolOther.tw(activity, "创建成功");
            if (rea != null)
                rea.reData(videoUpJson);
            if (onChangeCompleteListener != null)
                onChangeCompleteListener.changeComplete(videoUpJson, sonProject, sonPath);
        } else {
            ToolOther.tw(activity, "创建失败");
        }
        if (progressDialog != null)
            progressDialog.dismiss();

    }

    /**
     * 有地址数据
     * @param videoUpJson
     * @param onChangeCompleteListener
     * @param aMapLocation
     * @param name
     */
    public void addSonProject(VideoUpJson videoUpJson,
                              AMapLocation aMapLocation,
                              String name,
                              OnChangeCompleteListener onChangeCompleteListener
                              ) {
        addSonProject(videoUpJson,null,onChangeCompleteListener,aMapLocation,name,"暂无备注");
    }


    /**
     * 删除子节点
     *
     * @param rea         用于刷新列表
     * @param videoUpJson videoUpJson 用于写入数据
     */
    public VideoUpJson deleteSonProject(RecyclerAdapter rea, VideoUpJson videoUpJson, VideoUpJson.SonProject sonProject) {
        deleteFiles(new File(videoPath + videoUpJson.getProjectName() + "/" + sonProject.getSonProjectName()));
        videoUpJson.getListSon().remove(sonProject);
        writeJson(videoUpJson);
        rea.reData(videoUpJson);
        return videoUpJson;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////


    //定位一次
    public void mapOnce(OnMapListener onMapListener) {
        gaoDeStop = false;
        gaoDeP = 0;
        toolGaoDe = ToolGaoDe.getGaoDeTools(activity, aMapLocation -> {
            //暂停防止多余的回调
            if (gaoDeStop) return;
            //本次是否成功
            boolean isC = false;
            //判断定位成功
            if (aMapLocation != null && aMapLocation.getErrorCode()
                    == 0 && !aMapLocation.getAddress().equals("")) {
                isC = true;
            } else {
                // 定位失败
                gaoDeP++;
            }
            String ms = "定位失败";
            //失败重试
            if (isC) {
                toolGaoDe.stop();
                gaoDeStop = true;
                ms = aMapLocation.getAddress();
            } else if (gaoDeP < AppInfo.gaoDeInt) return;
            else {
                toolGaoDe.stop();
                gaoDeStop = true;
                if (onMapListener.mapError(ms))
                    return;
            }
            onMapListener.mapComplete(aMapLocation, ms);
        });
        //开始定位
        toolGaoDe.start();
    }


    //读取全部主项目包括子项目 //这里不能用其他软件删除项目
    public List<VideoUpJson> readProject() {

        String[] list1 = new File(videoPath).list((dir, name) ->
                dir.isDirectory() && new File(videoPath + name + "/" + AppInfo.videoProjectName).exists());
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
                assert dt1 != null;
                assert dt2 != null;
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


    //读取文件
    public static String readFile(String path) {
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
    public static void deleteFiles(File file) {
        if (file == null || !file.exists()) return;
        File[] files = file.listFiles();
        for (File f : files)
            if (f.isDirectory()) deleteFiles(f);
            else f.delete();
        file.delete();
    }

    public static boolean deleteFile(String path){
        File file = new File(path);
        if(file.exists())
            return file.delete();
        return false;
    }

    public  boolean deleteFile(String videoProjectName,String sonProjectName,String videoName){
        return deleteFile(videoPath + videoProjectName + "/" + sonProjectName + "/" + videoName);
    }




    //创建文件夹
    public static boolean createFiles(String path) {
        //创建文件夹
        File file = new File(path);
        if (!file.exists())
            return file.mkdirs();
        return true;
    }


    //写入项目json 数据
    public static boolean writeJson(VideoUpJson videoUpJson, String path) {
        //Gson 转 String
        String r = new Gson().toJson(videoUpJson);
        try {
            Writer out = new FileWriter(new File(path));
            out.write(r);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //写入/更新  项目json 数据
    public boolean writeJson(VideoUpJson videoUpJson) {
        return writeJson(videoUpJson, videoPath + videoUpJson.getProjectName() + "/" + AppInfo.videoProjectName);
    }


    //读取当前项目数据转对象
    public VideoUpJson readToVideoUpJson(String projectName) {
        String s = readFile(videoPath + projectName + "/" + AppInfo.videoProjectName);
        return new Gson().fromJson(s, VideoUpJson.class);
    }


}
