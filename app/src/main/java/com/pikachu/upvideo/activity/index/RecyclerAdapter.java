package com.pikachu.upvideo.activity.index;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final OnClickAndLongClickListener onClickAndLongClickListener;
    private List<VideoUpJson> videoUpJsons;


    public interface OnClickAndLongClickListener{
        void onClick(View view,VideoUpJson videoUpJson, int position);
        boolean onLongClick(View view,VideoUpJson videoUpJson, int position);
    }

    public RecyclerAdapter(List<VideoUpJson> videoUpJsons,
                           OnClickAndLongClickListener onClickAndLongClickListener) {
        this.videoUpJsons = videoUpJsons;
        this.onClickAndLongClickListener = onClickAndLongClickListener;
    }

    //更新数据
    public void reData(List<VideoUpJson> videoUpJsons) {
        if (this.videoUpJsons == null)
            this.videoUpJsons = new ArrayList<>();
        this.videoUpJsons = videoUpJsons;
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_index_project, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VideoUpJson videoUpJson = videoUpJsons.get(position);

        //状态
        holder.uiPText1.setVisibility(videoUpJson.getProjectType() == 0 ? View.VISIBLE : View.GONE);
        holder.uiPText2.setVisibility(videoUpJson.getProjectType() == 1 ? View.VISIBLE : View.GONE);
        holder.uiPText3.setVisibility(videoUpJson.getProjectType() == 2 ? View.VISIBLE : View.GONE);
        //name
        holder.uiPText4.setText(videoUpJson.getProjectName());
        //地址
        holder.uiPText5.setText(videoUpJson.getProjectAddress());
        //分段
        holder.uiPText6.setText(fdStr(videoUpJson));
        //视频比例
        holder.uiPText7.setText(popStr(videoUpJson));
        //视频清晰度
        holder.uiPText8.setText(pxStr(videoUpJson));
        //时间
        holder.uiPText9.setText(videoUpJson.getProjectTime());
        //事件
        holder.uiPLin.setOnClickListener(v ->
                onClickAndLongClickListener.onClick(v,videoUpJson,position));
        holder.uiPLin.setOnLongClickListener(v ->
                onClickAndLongClickListener.onLongClick(v,videoUpJson,position));
    }

    private String pxStr(VideoUpJson videoUpJson) {

        switch (videoUpJson.getProjectVideoPx()){
            case 0:
                return "720P 30FPS";
            case 1:
                return "480P 30FPS";
            case 2:
                return "1080P 30FPS";
        }
        return "720P 30FPS";
    }


    private String fdStr(VideoUpJson videoUpJson){
        int mode = videoUpJson.getProjectMode();
        String fd = "";
        if (mode == 0)
            fd = "不分段";
        else if (mode == 1)
            fd = "手动分段";
        else if (mode == 2)
            fd = "按距离分段\n" + videoUpJson.getProjectModeInfo() + "m/段" ;
        else if (mode == 3)
            fd = "按时间分段\n" + videoUpJson.getProjectModeInfo() + "min/段" ;
        return fd;
    }


    //比例
    private String popStr(VideoUpJson videoUpJson) {
        switch (videoUpJson.getProjectVideoWh()){
            case 0:
                return "4 : 3";
            case 1:
                return "16 : 9";
        }
        return "4 : 3";
    }



    @Override
    public int getItemCount() {
        return videoUpJsons.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout uiPLin;
        public TextView uiPText1;
        public TextView uiPText2;
        public TextView uiPText3;
        public TextView uiPText4;
        public TextView uiPText5;
        public TextView uiPText6;
        public TextView uiPText7;
        public TextView uiPText8;
        public TextView uiPText9;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uiPLin = itemView.findViewById(R.id.ui_p_lin);
            uiPText1 = itemView.findViewById(R.id.ui_p_text1);
            uiPText2 = itemView.findViewById(R.id.ui_p_text2);
            uiPText3 = itemView.findViewById(R.id.ui_p_text3);
            uiPText4 = itemView.findViewById(R.id.ui_p_text4);
            uiPText5 = itemView.findViewById(R.id.ui_p_text5);
            uiPText6 = itemView.findViewById(R.id.ui_p_text6);
            uiPText7 = itemView.findViewById(R.id.ui_p_text7);
            uiPText8 = itemView.findViewById(R.id.ui_p_text8);
            uiPText9 = itemView.findViewById(R.id.ui_p_text9);
        }
    }





}
