package com.pikachu.upvideo.activity.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.util.tools.ToolOther;
import com.pikachu.upvideo.util.tools.ToolVideoThumbnail;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterItem extends RecyclerView.Adapter<RecyclerAdapterItem.ViewHolder> {


    private Context context;
    private VideoUpJson videoUpJson;
    private VideoUpJson.SonProject sonProject;
    private RecyclerAdapter.OnClickListener onClickListener;
    private List<VideoUpJson.SonProject.ListHisVideo> sonProjectVideo;


    public RecyclerAdapterItem(Context context,
                               VideoUpJson videoUpJson,
                               VideoUpJson.SonProject sonProject,
                               RecyclerAdapter.OnClickListener onClickListener) {
        this.videoUpJson = videoUpJson;
        this.context = context;
        this.sonProject = sonProject;
        if (this.sonProject != null) sonProjectVideo =  this.sonProject.getSonProjectVideo();
        else sonProjectVideo = new ArrayList<>();

        this.onClickListener = onClickListener;
    }

    //更新数据
    public void reData(VideoUpJson.SonProject sonProject) {
        this.sonProject = sonProject;
        if (this.sonProject != null) sonProjectVideo = this.sonProject.getSonProjectVideo();
        else sonProjectVideo = new ArrayList<>();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_son_item_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        VideoUpJson.SonProject.ListHisVideo listHisVideo = sonProjectVideo.get(position);


        //时间
        holder.uiSonVideoText.setText("<" + (sonProjectVideo.size() - position) + "> " + listHisVideo.getVideoTime());


        //第一帧 图片
        //holder.uiSonVideoImage.setImageBitmap();
       /* Bitmap videoThumbnail = ToolVideoThumbnail.getVideoThumbnail(
                context,
                videoUpJson.getProjectName(),
                sonProject.getSonProjectName(), listHisVideo.getVideoName());*/

       /* Bitmap  videoThumbnail =  ToolVideoThumbnail.decodeFrame(1,
                context,
                videoUpJson.getProjectName(),
                sonProject.getSonProjectName(),
                listHisVideo.getVideoName());

        if (videoThumbnail != null)
            holder.uiSonVideoImage.setImageBitmap(videoThumbnail);*/


        String s = ToolOther.getVideoPath(context) +
                videoUpJson.getProjectName() + "/" +
                sonProject.getSonProjectName() + "/" +
                listHisVideo.getVideoName() + ".mp4";
        Glide.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                                .error(R.drawable.ic_author)
                                .skipMemoryCache(true) // 不使用内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                )
                .load(s)
                .into( holder.uiSonVideoImage);





        //事件
        if (onClickListener != null) {
            holder.uiSonVideoLin.setOnClickListener(v ->
                    onClickListener.onMinClick(v, sonProject, listHisVideo, position));
            holder.uiSonVideoLin.setOnLongClickListener(v ->
                    onClickListener.onMinLongClick(v, sonProject, listHisVideo, position));
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
