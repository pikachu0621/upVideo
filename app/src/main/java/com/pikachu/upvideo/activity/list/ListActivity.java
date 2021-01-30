package com.pikachu.upvideo.activity.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pikachu.upvideo.R;
import com.pikachu.upvideo.activity.camera.CameraActivity;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.util.AppInfo;
import com.pikachu.upvideo.util.base.BaseActivity;

public class ListActivity extends BaseActivity {

    private RecyclerView list;
    private Toolbar barToolbar;
    private VideoUpJson videoUpJson;

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
    }

    private void initView() {
        list = findViewById(R.id.list_recycler);
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

                //进入拍摄
                Intent intent = new Intent(this, CameraActivity.class);
                intent.putExtra(AppInfo.START_ACTIVITY_KEY_1, videoUpJson);
                intent.putExtra(AppInfo.START_ACTIVITY_KEY_2, AppInfo.START_CAMERA_TYPE_1);
                startActivity(intent);

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




}