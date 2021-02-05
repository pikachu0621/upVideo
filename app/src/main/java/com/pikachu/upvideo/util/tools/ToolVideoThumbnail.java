package com.pikachu.upvideo.util.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;

import java.util.HashMap;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;

/**
 * @author Pikachu
 * @Project upVideo
 * @Package com.pikachu.upvideo.util.tools
 * @Date 2021/2/5 ( 下午 7:27 )
 * @description 获取视频略说图
 */
public class ToolVideoThumbnail {


    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */

    private static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public static Bitmap getVideoThumbnail(Context context, String projectName, String sonProjectName, String videoName) {
        String path = ToolOther.getVideoPath(context) + projectName + "/" + sonProjectName + "/" + videoName + ".mp4";
        return getVideoThumbnail(path, 240, 160, MINI_KIND);
    }


    /**
     * 获取视频某一帧	 * @param timeMs 毫秒	 * @param listener
     */
    public static Bitmap decodeFrame(long timeMs, Context context, String projectName, String sonProjectName, String videoName) {

        try {
            String path = ToolOther.getVideoPath(context) + projectName + "/" + sonProjectName + "/" + videoName + ".mp4";
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(path);


            return  mediaMetadataRetriever.getFrameAtTime(timeMs * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
        }catch (Exception e){

            return null;
        }


    }


}
