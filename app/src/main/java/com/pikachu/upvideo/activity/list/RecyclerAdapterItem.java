package com.pikachu.upvideo.activity.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterItem extends RecyclerView.Adapter<RecyclerAdapterItem.ViewHolder> {


    private VideoUpJson.SonProject sonProject;
    private RecyclerAdapter.OnClickListener onClickListener;
    private List<VideoUpJson.SonProject.ListHisVideo> sonProjectVideo;


    public RecyclerAdapterItem(VideoUpJson.SonProject sonProject,
                               RecyclerAdapter.OnClickListener onClickListener) {

        this.sonProject = sonProject;
        if (this.sonProject != null) sonProjectVideo = sonProject.getSonProjectVideo();
        else sonProjectVideo = new ArrayList<>();

        this.onClickListener = onClickListener;
    }

    //更新数据
    public void reData(VideoUpJson.SonProject sonProject) {
        this.sonProject = sonProject;
        if (this.sonProject != null) sonProjectVideo = sonProject.getSonProjectVideo();
        else sonProjectVideo = new ArrayList<>();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_son_item_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        VideoUpJson.SonProject.ListHisVideo listHisVideo = sonProjectVideo.get(position);


        //时间
        holder.uiSonVideoText.setText(listHisVideo.getVideoTime());
        //第一帧 图片
        //holder.uiSonVideoImage.setImageBitmap();
        //事件
        if (onClickListener != null ){
            holder.uiSonVideoLin.setOnClickListener(v ->
                    onClickListener.onMinClick(v, sonProject, listHisVideo, position));
            holder.uiSonVideoLin.setOnLongClickListener(v ->
                    onClickListener.onMinLongClick(v,sonProject, listHisVideo, position));
        }

    }


    @Override
    public int getItemCount() {
        return sonProjectVideo.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout uiSonVideoLin;
        public TextView uiSonVideoText;
        public ImageView uiSonVideoImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uiSonVideoLin = itemView.findViewById(R.id.ui_son_video_lin);
            uiSonVideoText = itemView.findViewById(R.id.ui_son_video_text);
            uiSonVideoImage = itemView.findViewById(R.id.ui_son_video_image);
        }
    }

   /* public void setOnClickListener(RecyclerAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }*/

}
