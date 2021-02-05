package com.pikachu.upvideo.cls;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import java.io.Serializable;

/**
 * @author Pikachu
 * @Project upVideo
 * @Package com.pikachu.upvideo.cls
 * @Date 2021/2/4 ( 下午 8:53 )
 * @description
 */
public class CameraStartData implements Serializable {

    public static final int START_CAMERA_TYPE_1  = 1 , START_CAMERA_TYPE_2  = 2 ;

    private int startType; // 1(添加) ,2（重拍）
    private boolean isIndex; // 是否为第一页跳转
    private String sonPath;
    private VideoUpJson videoUpJson;
    private VideoUpJson.SonProject sonProject;
    private VideoUpJson.SonProject.ListHisVideo listHisVideo;

    public CameraStartData(@IntRange(from = 1,to = 2) int startType,
                           @NonNull boolean isIndex,
                           @NonNull String sonPath,
                           @NonNull VideoUpJson videoUpJson,
                           @NonNull VideoUpJson.SonProject sonProject,
                           VideoUpJson.SonProject.ListHisVideo listHisVideo) {
        this.startType = startType;
        this.isIndex = isIndex;
        this.sonPath = sonPath;
        this.videoUpJson = videoUpJson;
        this.sonProject = sonProject;
        this.listHisVideo = listHisVideo;
    }


    public CameraStartData() { }


    public int getStartType() {
        return startType;
    }

    public void setStartType(@IntRange(from = 1,to = 2) int startType) {
        this.startType = startType;
    }

    public boolean isIndex() {
        return isIndex;
    }

    public void setIndex(boolean index) {
        isIndex = index;
    }

    public String getSonPath() {
        return sonPath;
    }

    public void setSonPath(@NonNull String sonPath) {
        this.sonPath = sonPath;
    }

    public VideoUpJson getVideoUpJson() {
        return videoUpJson;
    }

    public void setVideoUpJson(@NonNull VideoUpJson videoUpJson) {
        this.videoUpJson = videoUpJson;
    }

    public VideoUpJson.SonProject getSonProject() {
        return sonProject;
    }

    public void setSonProject(@NonNull VideoUpJson.SonProject sonProject) {
        this.sonProject = sonProject;
    }

    public VideoUpJson.SonProject.ListHisVideo getListHisVideo() {
        return listHisVideo;
    }

    public void setListHisVideo(VideoUpJson.SonProject.ListHisVideo listHisVideo) {
        this.listHisVideo = listHisVideo;
    }
}
