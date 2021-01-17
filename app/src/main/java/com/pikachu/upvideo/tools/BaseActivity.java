package com.pikachu.upvideo.tools;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.PermissionDef;




public class BaseActivity  extends AppCompatActivity {


    public interface OnPermissionListener{
        void onGranted();//授予
        void onDenied();//拒绝
    }

    public Toast showToast(String msg){
        return Tools.tw(this,msg);
    }



    //权限申请
    public void sendPermission(OnPermissionListener onPermissionListener){
        sendPermission(onPermissionListener,AppInfo.permissions);
    }

    //权限申请
    public void sendPermission(OnPermissionListener onPermissionListener,
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



}
