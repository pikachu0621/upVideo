package com.pikachu.upvideo.activity.up;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.util.tools.ToolAddProjects;
import java.util.ArrayList;
import java.util.List;

public class RecyclerTaskAdapter extends RecyclerView.Adapter<RecyclerTaskAdapter.ViewHolder> {


    private final Activity activity;
    private final OnClickListener onClickListener;
    private List<VideoUpJson> projectList;


    public interface OnClickListener {
        void onStopClick(View view, VideoUpJson videoUpJson, List<VideoUpJson> projectList, int position);
        void onListClick(View view, VideoUpJson videoUpJson, List<VideoUpJson> projectList, int position);
        boolean onLongListClick(View view, VideoUpJson videoUpJson, List<VideoUpJson> projectList, int position);
    }

    public RecyclerTaskAdapter(Activity activity,
                               List<VideoUpJson> projectList,
                               OnClickListener onClickListener) {
        this.activity = activity;
        if (projectList != null)
            this.projectList = projectList;
        else
            this.projectList = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    //更新数据
    public void reData(List<VideoUpJson> projectList) {
        if (projectList != null)
            this.projectList = projectList;
        else this.projectList = new ArrayList<>();
        notifyDataSetChanged();
    }








    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_task_item, parent, false));
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VideoUpJson videoUpJson = projectList.get(position);


        holder.uiTaskText1.setText(videoUpJson.getProjectName());
        holder.uiTaskText6.setText(videoUpJson.getProjectName());

        List<String> projectVideoPath = ToolAddProjects.getProjectVideoPath(activity, videoUpJson);
        if (projectVideoPath == null || projectVideoPath.size() <= 0) {
            holder.uiTaskLin2.setVisibility(GridView.VISIBLE);
            holder.uiTaskLin1.setVisibility(GridView.GONE);
        }else {
            holder.uiTaskLin1.setVisibility(GridView.VISIBLE);
            holder.uiTaskLin2.setVisibility(GridView.GONE);


            //模拟
            holder.uiTaskText2.setText("正在鸭缩视频中...");
            holder.uiTaskText3.setText("0%");
            holder.uiTaskText4.setText("0/" + projectVideoPath.size());
            holder.uiTaskProgress.setMax(projectVideoPath.size());
            holder.uiTaskProgress.setProgress(0);
        }




        //事件
        if (onClickListener != null ){
            holder.uiTaskText5.setOnClickListener(v ->
                    onClickListener.onStopClick(v,videoUpJson,projectList,position));
            holder.uiTaskRelative.setOnClickListener(v ->
                    onClickListener.onListClick(v,videoUpJson,projectList,position));
            holder.uiTaskRelative.setOnLongClickListener(v ->
                    onClickListener.onLongListClick(v,videoUpJson,projectList,position));
        }


    }


    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout uiTaskRelative;
        private final LinearLayout uiTaskLin1,uiTaskLin2;
        private final TextView uiTaskText1;
        private final TextView uiTaskText2;
        private final TextView uiTaskText3;
        private final ProgressBar uiTaskProgress;
        private final TextView uiTaskText4;
        private final TextView uiTaskText5;
        private final TextView uiTaskText6;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uiTaskRelative = itemView.findViewById(R.id.ui_task_relative);
            uiTaskLin1 = itemView.findViewById(R.id.ui_task_lin1);
            uiTaskText1 = itemView.findViewById(R.id.ui_task_text1);
            uiTaskText2 = itemView.findViewById(R.id.ui_task_text2);
            uiTaskText3 = itemView.findViewById(R.id.ui_task_text3);
            uiTaskProgress = itemView.findViewById(R.id.ui_task_progress);
            uiTaskText4 = itemView.findViewById(R.id.ui_task_text4);
            uiTaskText5 = itemView.findViewById(R.id.ui_task_text5);
            uiTaskLin2 = itemView.findViewById(R.id.ui_task_lin2);
            uiTaskText6 = itemView.findViewById(R.id.ui_task_text6);


        }
    }

}
