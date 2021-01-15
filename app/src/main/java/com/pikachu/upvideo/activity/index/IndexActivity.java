package com.pikachu.upvideo.activity.index;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.init.AddProjects;
import com.pikachu.upvideo.tools.BaseActivity;

import java.util.List;

public class IndexActivity extends BaseActivity implements BaseActivity.OnPermissionListener, NavigationView.OnNavigationItemSelectedListener {

    private Toolbar indexToolbar;
    private NavigationView indexNav;
    private DrawerLayout indexDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private SearchView mSearchView;
    private AddProjects addProject;
    private NullFragment nullFragment;
    private FragmentManager supportFragmentManager;
    private List<VideoUpJson> videoUpJsons;
    private FloatingActionButton indexFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
        init();
    }


    @SuppressLint("WrongConstant")
    private void init() {
        setActionBar();
        supportFragmentManager = getSupportFragmentManager();


        //权限申请
        sendPermission(this);
        //读取项目
        addProject = AddProjects.getAddProject(this);


        //项目显示
        videoUpJsons = addProject.readProject();
        if (videoUpJsons.size() > 0) {
            //项目list


        } else {
            nullFragment = new NullFragment();
            supportFragmentManager.beginTransaction().replace(R.id.index_frame, nullFragment).commit();
        }


        //添加项目
        indexFloatingActionButton.setOnClickListener(v ->{
            if (isPermission())
                addProject.addProject();
            else
                showToast("权限不足");
        });
    }


    private void setActionBar() {
        indexToolbar.setTitle(R.string.app_name);
        setSupportActionBar(indexToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) return;
        supportActionBar.setHomeButtonEnabled(true); //设置返回键可用
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        indexNav.setNavigationItemSelectedListener(this);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, indexDrawer, indexToolbar, R.string.app_author, R.string.app_name);
        mDrawerToggle.syncState();
        indexDrawer.addDrawerListener(mDrawerToggle);
    }


    private void initView() {
        indexToolbar = findViewById(R.id.index_toolbar);
        indexNav = findViewById(R.id.index_nav);
        indexDrawer = findViewById(R.id.index_drawer);
        indexFloatingActionButton = findViewById(R.id.index_floatingActionButton);
    }


    //toolbar菜单
    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        mSearchView.setMaxWidth(780);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //这里匹配结果
                showToast(newText);
                Log.i("test_t", newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    //菜单点击事件
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                showToast("1");
                break;
            case R.id.item2:
                showToast("2");
                break;
        }
        return false;
    }

    //侧滑点击
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item1:
                showToast("1");
                break;
            case R.id.nav_item2:
                if (isPermission())
                    showToast("2");
                else
                    showToast("权限不足");
                break;
            case R.id.nav_item3:
                showToast("3");
                break;
            case R.id.nav_item4:
                showToast("4");
                break;
        }

        return true;
    }


    @Override
    public void onGranted() {
        //权限授予

    }

    @Override
    public void onDenied() {
        showToast("权限不足");
        //finish();
    }


}