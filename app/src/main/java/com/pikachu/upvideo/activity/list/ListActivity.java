package com.pikachu.upvideo.activity.list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.util.AppInfo;
import com.pikachu.upvideo.util.base.BaseActivity;
import com.pikachu.upvideo.util.tools.ToolAddProjects;

public class ListActivity extends BaseActivity implements RecyclerAdapter.OnClickListener {

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
        setHead(true,videoUpJson.getProjectName(),null,null);
        //添加项目
        toolAddProjects = new ToolAddProjects(this);

        list();
    }





    private void list() {
        sw.setOnRefreshListener(() -> sw.postDelayed(() -> sw.setRefreshing(false),1000));

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
                        videoUpJson -> ListActivity.this.videoUpJson = videoUpJson);

                //进入拍摄
                /*Intent intent = new Intent(this, CameraActivity.class);
                intent.putExtra(AppInfo.START_ACTIVITY_KEY_1, videoUpJson);
                intent.putExtra(AppInfo.START_ACTIVITY_KEY_2, AppInfo.START_CAMERA_TYPE_1);
                startActivity(intent);*/

                break;
            case R.id.list_item1:
                showToast("同步此项目");
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






    ///////////////////////////////////点击事件///////////////////////////////////////////////////
    @Override
    public void onMaxClick(View view, VideoUpJson.SonProject sonProject, int position) {
        //外部 大item 点击事件 删除本地节点

    }

    @Override
    public void onMinClick(View view, VideoUpJson.SonProject.ListHisVideo listHisVideo, int position) {
        //内部 小item 点击事件
    }

    @Override
    public boolean onMaxLongClick(View view, VideoUpJson.SonProject sonProject, int position) {
        //外部 大item 长按事件
        toolAddProjects.deleteSonProject(recyclerAdapter,videoUpJson,sonProject);
        return true;
    }

    @Override
    public boolean onMinLongClick(View view, VideoUpJson.SonProject.ListHisVideo listHisVideo, int position) {
        //内部 小item 长按事件
        return false;
    }
}