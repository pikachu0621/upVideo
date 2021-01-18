package com.pikachu.upvideo.util.base;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.pikachu.upvideo.util.AppInfo;
import com.pikachu.upvideo.util.state.PKStatusBarActivity;
import com.pikachu.upvideo.util.state.PKStatusBarTools;
import com.pikachu.upvideo.util.tools.ToolOther;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.PermissionDef;


public abstract class BaseActivity extends PKStatusBarActivity {


    private Toast toast;
    private OnBackEvent onBackEvent;
    private Intent intent;
    private Value value;
    private Point point;



    public interface OnPermissionListener{
        void onGranted();//授予
        void onDenied();//拒绝
    }

    public Toast showToast(String msg){
        return ToolOther.tw(this,msg);
    }



    //权限申请
    public void sendPermission(com.pikachu.upvideo.util.base.BaseActivity.OnPermissionListener onPermissionListener){
        sendPermission(onPermissionListener, AppInfo.permissions);
    }

    //权限申请
    public void sendPermission(com.pikachu.upvideo.util.base.BaseActivity.OnPermissionListener onPermissionListener,
                               @NonNull @PermissionDef String... permissions){

        if (AndPermission.hasPermissions(this, permissions)){
            onPermissionListener.onGranted();
        }else {
            AndPermission.with(this)
                    .runtime()
                    .permission(permissions)
                    .onGranted(data -> onPermissionListener.onGranted())
                    .onDenied(data -> onPermissionListener.onDenied())
                    .start();
        }
    }

    //判断权限是否有
    public boolean isPermission(@NonNull @PermissionDef String... permissions){
        if (AndPermission.hasPermissions(this, permissions))
            return true;
        return false;
    }
    //判断权限是否有
    public boolean isPermission(){
        return isPermission(AppInfo.permissions);
    }











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPkStatusBarTools(pkStatusBarTools());
    }




    //设置沉浸式状态栏 //返回null为不设置
    public PKStatusBarTools pkStatusBarTools(){
        PKStatusBarTools pkStatusBarTools = PKStatusBarTools.with(this).noToNON();
        pkStatusBarTools.init();
        return pkStatusBarTools;
    }


    public void setContentView(@LayoutRes int layoutResID,@IdRes int... statusResID) {
        super.setContentView(layoutResID);
        if (statusResID.length > 0)
            for (int id :statusResID)
                setPlaceholderView(id);
    }


    /**
     * 设置监听
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home || onBackEvent != null)
            onBackEvent.backEvent();
        return super.onOptionsItemSelected(item);
    }



    /**
     * 返回键实现 接口
     */
    public interface OnBackEvent {
        void backEvent();
    }


    /**
     * 设置md ActionBar 头部
     *
     * @param isShowHome  是否显示返回键
     * @param title       设置标题 null 不设置
     * @param subTitle    设置复标题 null 不设置
     * @param onBackEvent 设置 返回键的监听 null 不设置
     * @return
     */
    public ActionBar setHead(boolean isShowHome, String title,
                             String subTitle, OnBackEvent onBackEvent) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(isShowHome);
            if (title != null)
                actionBar.setTitle(title);
            if (subTitle != null)
                actionBar.setSubtitle(subTitle);
            this.onBackEvent = onBackEvent;
        }
        return actionBar;
    }



    /**
     * 简单的  Activity 接收信息
     */
    public String getStringExtra(String name) {
        intent = getIntent();
        return intent.getStringExtra(name);
    }

    public int getIntExtra(String name, int defaultValue) {
        intent = getIntent();
        return intent.getIntExtra(name, defaultValue);
    }

    public float getFloatExtra(String name, float defaultValue) {
        intent = getIntent();
        return intent.getFloatExtra(name, defaultValue);
    }


    public <T> T getSerializableExtra(String name,Class<T> cls) {
        return  (T) getIntent().getSerializableExtra(name);
    }




    public static class Value {
        public int x, y;
    }

    /**
     * 获取屏幕 宽高
     * @return
     */
    public Value getScreenSize() {
        if (point == null)
            point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        if (value == null)
            value = new Value();
        value.x = point.x;
        value.y = point.y;
        return value;
    }


    /**
     * 设置一个占位view 用于占位状态栏
     *
     * @param view
     */
    public void setPlaceholderView(View view){
        ToolOther.setNonHigh(this, view);
    }

    /**
     * 设置一个占位view 用于占位状态栏
     * @param id
     */
    public void setPlaceholderView(@IdRes int id){
        ToolOther.setNonHigh(this, findViewById(id));
    }



}
