package com.pikachu.upvideo.activity.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pikachu.upvideo.R;
import com.pikachu.upvideo.activity.camera.CameraActivity;
import com.pikachu.upvideo.activity.up.UpZipVideoActivity;
import com.pikachu.upvideo.cls.CameraStartData;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.util.AppInfo;
import com.pikachu.upvideo.util.base.BaseActivity;
import com.pikachu.upvideo.util.tools.ToolAddProjects;
import com.pikachu.upvideo.util.tools.ToolOther;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends BaseActivity implements RecyclerAdapter.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView list;
    private SwipeRefreshLayout sw;
    private Toolbar barToolbar;
    private VideoUpJson videoUpJson;
    private ToolAddProjects toolAddProjects;
    private RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list, R.id.bar_view);
        initView();
        init();
    }

    private void init() {
        videoUpJson = getSerializableExtra(AppInfo.START_ACTIVITY_KEY_1, VideoUpJson.class);
        setSupportActionBar(barToolbar);
        setHead(true, videoUpJson.getProjectName(), null, null);
        //添加项目
        toolAddProjects = new ToolAddProjects(this);

        list();
    }


    private void list() {
        sw.setOnRefreshListener(this);

        recyclerAdapter = new RecyclerAdapter(this, videoUpJson, this);
        list.setAdapter(recyclerAdapter);
        list.setLayoutManager(new LinearLayoutManager(this));
    }


    private void initView() {
        list = findViewById(R.id.list_recycler);
        sw = findViewById(R.id.list_sw);
        barToolbar = findViewById(R.id.bar_toolbar);
    }


    //toolbar菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //菜单点击事件
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.list_item0:
                showToast("添加节点");

                //  添加子项目
                toolAddProjects.addSonProject(recyclerAdapter, videoUpJson,
                        (videoUpJson, sonProject, sonPath) -> {
                            ListActivity.this.videoUpJson = videoUpJson;
                            startActivityCamera(1,sonPath,videoUpJson,sonProject,null);
                        }, true);

                //进入拍摄
                /*Intent intent = new Intent(this, CameraActivity.class);
                intent.putExtra(AppInfo.START_ACTIVITY_KEY_1, videoUpJson);
                intent.putExtra(AppInfo.START_ACTIVITY_KEY_2, AppInfo.START_CAMERA_TYPE_1);
                startActivity(intent);*/

                break;
            case R.id.list_item1:

                showToast("同步此项目");
                List<VideoUpJson> videoUpJsons = new ArrayList<>();
                videoUpJsons.add(videoUpJson);
                videoUpJsons.add(videoUpJson);
                videoUpJsons.add(videoUpJson);

                Intent intent = new Intent(this, UpZipVideoActivity.class);
                intent.putExtra(AppInfo.START_ACTIVITY_KEY_1, videoUpJsons.toArray(new VideoUpJson[]{}));
                startActivity(intent);

                break;
            case R.id.list_item2:
                showToast("全选");
                break;
            case R.id.list_item3:
                showToast("删除");
                break;
        }
        return false;
    }



    private void startActivityCamera(@IntRange(from = 1,to = 2) int startType,
                                     @NonNull String sonPath,
                                     @NonNull VideoUpJson videoUpJson,
                                     @NonNull VideoUpJson.SonProject sonProject,
                                     VideoUpJson.SonProject.ListHisVideo listHisVideo){
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(AppInfo.START_ACTIVITY_KEY_1, videoUpJson);
        intent.putExtra(AppInfo.START_ACTIVITY_KEY_2,
                new CameraStartData(startType,false, sonPath, videoUpJson, sonProject, listHisVideo));
        startActivity(intent);

    }

    ///////////////////////////////////点击事件///////////////////////////////////////////////////
    @Override
    public void onMaxClick(View view, VideoUpJson.SonProject sonProject, int position) {
        //外部 大item 点击事件

    }

    @Override
    public void onMinClick(View view, VideoUpJson.SonProject sonProject, VideoUpJson.SonProject.ListHisVideo listHisVideo, int position) {
        //内部 小item 点击事件
        //点击添加视频
        String sonPath = ToolOther.getVideoPath(this) + videoUpJson.getProjectName() + "/" /* + sonProject.getSonProjectName()*/;
        startActivityCamera(2,sonPath,videoUpJson,sonProject,listHisVideo);
    }

    @Override
    public void onAddClick(View view, VideoUpJson.SonProject sonProject, int position) {
        //点击添加视频
        String sonPath = ToolOther.getVideoPath(this) + videoUpJson.getProjectName() + "/" /* + sonProject.getSonProjectName()*/;
        startActivityCamera(1,sonPath,videoUpJson,sonProject,null);
    }

    @Override
    public boolean onMaxLongClick(View view, VideoUpJson.SonProject sonProject, int position) {
        //外部 大item 长按事件
        //删除本地节点
        toolAddProjects.deleteSonProject(recyclerAdapter, videoUpJson, sonProject);
        return true;
    }

    @Override
    public boolean onMinLongClick(View view, VideoUpJson.SonProject sonProject, VideoUpJson.SonProject.ListHisVideo listHisVideo, int position) {
        //内部 小item 长按事件
        return false;
    }


    private void reData(){
        this.videoUpJson = toolAddProjects.readToVideoUpJson(this.videoUpJson.getProjectName());
        recyclerAdapter.reData(videoUpJson);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reData();
    }

    @Override
    public void onRefresh() {
        reData();
        sw.setRefreshing(false);
    }
}