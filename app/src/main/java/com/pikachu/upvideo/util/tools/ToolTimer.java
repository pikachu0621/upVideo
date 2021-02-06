package com.pikachu.upvideo.util.tools;

import android.annotation.SuppressLint;
import android.app.Activity;

/**
 * @author Pikachu
 * @Project upVideo
 * @Package com.pikachu.upvideo.util.tools
 * @Date 2021/1/29 ( 下午 6:54 )
 * @description 计时器/时间工具
 */
public class ToolTimer {


    private Activity activity;
    private OnTimeRunListener onTimeRunListener = null;
    private String timeFormat;
    private long time = 0;
    private long gapItem = 1000;
    @SuppressLint("StaticFieldLeak")
    private static ToolTimer toolTimer;
    private Thread thread;


    public static class InitTime {
        public int hh, mm, ss;

        public InitTime(int hh, int mm, int ss) {
            this.hh = hh; // 时
            this.mm = mm; // 分
            this.ss = ss; // 秒
        }
    }

    public interface OnTimeRunListener {
        void runTime(String timeStr,long time); // 运行时
        void stopTime(String timeStr,long time); // 暂停
    }

    private final Runnable runnable = () -> {
        while (true){
            try {
                Thread.sleep(gapItem);
            } catch (InterruptedException e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> onTimeRunListener.stopTime(getTime(time), time));
                return;
            }
            time += gapItem;
            activity.runOnUiThread(() -> onTimeRunListener.runTime(getTime(time), time/gapItem));
        }
    };



    public static ToolTimer getTimer(Activity activity, long gapItem, String timeFormat , OnTimeRunListener onTimeRunListener) {
        toolTimer = new ToolTimer(activity, gapItem, timeFormat,onTimeRunListener);
        return toolTimer;
    }

    public static ToolTimer getTimer(Activity activity, OnTimeRunListener onTimeRunListener ) {
        return getTimer(activity, 1000, "00:00:00",onTimeRunListener);
    }


    public ToolTimer(Activity activity, long gapItem, String timeFormat , OnTimeRunListener onTimeRunListener) {
        this.activity = activity;
        this.gapItem = gapItem;
        this.timeFormat = timeFormat;
        this.onTimeRunListener = onTimeRunListener;
    }




    /**
     * 开始
     */
    public void run(){
        if (thread == null)
            thread = new Thread(runnable);
        thread.start();
    }

    /**
     * 暂停
     */
    public void stop(){
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    /**
     * 重置
     * @return
     */
    public void afresh(){
        stop();
        time = 0;
    }



    public static String getTime(long item) {

        item  =  item / 1000;
        InitTime initTime = new InitTime((int) (item / 3600), //小时
                (int) ((item % 3600) / 60) , //分钟
                (int) ((item % 3600) % 60)); //秒

        String time = "";

        if (initTime.hh <= 0)
            time += "";
        else if (initTime.hh < 10)
            time += "0"+initTime.hh +":";
        else
            time += initTime.hh +":";

        if (initTime.mm <= 0)
            time += "00:";
        else if (initTime.mm < 10)
            time += "0"+initTime.mm +":";
        else
            time += initTime.mm +":";

        if (initTime.ss <= 0)
            time += "00";
        else if (initTime.ss < 10)
            time += "0"+initTime.ss;
        else
            time += initTime.ss + "";
        return time;
    }



}
