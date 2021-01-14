package com.pikachu.upvideo.init;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class GaoDe {

    private final AMapLocationListener var1;
    //定位
    private AMapLocationClient mLocationClient;//声明AMapLocationClient类对象
    private AMapLocationClientOption mLocationOption;//声明AMapLocationClientOption对象

    private final Activity activity;
    @SuppressLint("StaticFieldLeak")
    private static GaoDe gaoDe;
    private boolean start;


    public static GaoDe getGaoDeTools(Activity activity) {
        if (gaoDe == null)
            gaoDe = new GaoDe(activity, aMapLocation -> {
            });
        return gaoDe;
    }

    public static GaoDe getGaoDeTools(Activity activity, AMapLocationListener var1) {
        if (gaoDe == null)
            gaoDe = new GaoDe(activity, var1);
        return gaoDe;
    }


    public GaoDe(Activity activity, AMapLocationListener var1) {
        this.activity = activity;
        this.var1 = var1;
        initGaoDe();
    }

    //初始高德定位
    private void initGaoDe() {
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(activity);
            //设置定位回调监听
            mLocationClient.setLocationListener(var1);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();

            //高精度定位
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //方向
            mLocationOption.setSensorEnable(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否允许模拟位置,默认为true，允许模拟位置
            mLocationOption.setMockEnable(true);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(1000);
        }
    }


    //开始定位
    public void start() {
        if (mLocationClient != null) {
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
            start = true;
        }
    }

    //结束定位
    public void stop() {
        if (mLocationClient != null) {
            //停止定位后，本地定位服务并不会被销毁
            mLocationClient.stopLocation();
            start = false;
        }
    }

    

    public boolean isStart(){
        return start;
    }
}
