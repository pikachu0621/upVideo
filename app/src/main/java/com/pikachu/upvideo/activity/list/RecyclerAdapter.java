package com.pikachu.upvideo.activity.list;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.cls.VideoUpJson.SonProject.ListHisVideo;
import com.pikachu.upvideo.cls.VideoUpJson.SonProject;
import com.pikachu.upvideo.util.tools.ToolOther;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    private final Activity activity;
    private OnClickListener onClickListener;
    private List<VideoUpJson.SonProject> listSon;
    private final int  LIST_BOTTOM = 2 , LIST_ONE = 3, LIST_TOW = 4;


    public interface OnClickListener {
        void onMaxClick(View view, SonProject sonProject, int position);
        void onMinClick(View view, ListHisVideo listHisVideo, int position);
        void onAddClick(View view,  SonProject sonProject,  int position);
        boolean onMaxLongClick(View view, SonProject sonProject, int position);
        boolean onMinLongClick(View view, ListHisVideo listHisVideo, int position);
    }

    public RecyclerAdapter(Activity activity,
                           VideoUpJson videoUpJsons,
                           OnClickListener onClickListener) {
        this.activity = activity;
        if (videoUpJsons != null)
            listSon = videoUpJsons.getListSon();
        else listSon = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    //更新数据
    public void reData(VideoUpJson videoUpJsons) {
        if (videoUpJsons != null)
            listSon = videoUpJsons.getListSon();
        else listSon = new ArrayList<>();
        notifyDataSetChanged();
    }







    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /*if (viewType == LIST_TOP)
            view =  from.inflate(R.layout.ui_son_project_item_top, parent, false);
        else if (viewType == LIST_BOTTOM)
            view =from.inflate(R.layout.ui_son_project_item_bottom, parent, false);*/
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_son_project_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        int itemViewType = getItemViewType(position);




        if (itemViewType == LIST_ONE) {
            holder.uiSonStart.setVisibility(View.VISIBLE);
            holder.uiSonEnd.setVisibility(View.VISIBLE);
            holder.uiSonEnd1.setVisibility(View.GONE);
        }else if (itemViewType == LIST_TOW){
            holder.uiSonStart.setVisibility(View.VISIBLE);
            holder.uiSonEnd.setVisibility(View.GONE);
            holder.uiSonEnd1.setVisibility(View.VISIBLE);
        }else {
            holder.uiSonStart.setVisibility(View.GONE);
            holder.uiSonEnd.setVisibility(itemViewType == LIST_BOTTOM ? View.VISIBLE : View.GONE);
            holder.uiSonEnd1.setVisibility(itemViewType != LIST_BOTTOM ? View.VISIBLE : View.GONE);
        }


        SonProject sonProject = listSon.get(position);
        //name
        holder.uiSonText1.setText(sonProject.getSonProjectName());
        //time
        holder.uiSonText5.setText(ToolOther.getTime(
                sonProject.getSonProjectStartMapInfo().getTime()
        ));
        //add video
        holder.uiSonAddProject.setOnClickListener(v -> onClickListener.onAddClick(v,sonProject,position));

        //from
        String fromStr = "经度：" + sonProject.getSonProjectStartMapInfo().getLongitude() + "\n" +
                "纬度：" + sonProject.getSonProjectStartMapInfo().getLatitude() + "\n" +
                sonProject.getSonProjectStartMapInfo().getAddress();
        holder.uiSonText2.setText(fromStr);
        //to
        String toStr = "经度：" + sonProject.getSonProjectEndMapInfo().getLongitude() + "\n" +
                "纬度：" + sonProject.getSonProjectEndMapInfo().getLatitude() + "\n" +
                sonProject.getSonProjectEndMapInfo().getAddress();
        holder.uiSonText3.setText(toStr);
        //备注
        holder.uiSonText4.setText(sonProject.getSonProjectMsg());


        //视频
        RecyclerAdapterItem recyclerAdapterItem =
                new RecyclerAdapterItem(sonProject, onClickListener);
        holder.uiSonRecycler.setAdapter(recyclerAdapterItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.uiSonRecycler.setLayoutManager(layoutManager);


        //事件
        if (onClickListener != null){
            holder.uiSonLin.setOnClickListener(v ->
                    onClickListener.onMaxClick(v,sonProject,position));
            holder.uiSonLin.setOnLongClickListener(v ->
                    onClickListener.onMaxLongClick(v,sonProject,position));
        }


    }


    @Override
    public int getItemCount() {
        return listSon.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0 && listSon.size() == 1)
            return LIST_ONE;
        else if (position == 0 && listSon.size() > 1)
            return LIST_TOW;
        else if (position == listSon.size() - 1)
            return LIST_BOTTOM;
        return super.getItemViewType(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout uiSonLin;
        public TextView uiSonText1;
        public TextView uiSonText5;
        public ImageView uiSonAddProject;
        public TextView uiSonText2;
        public TextView uiSonText3;
        public TextView uiSonText4;
        public RecyclerView uiSonRecycler;
        public View uiSonStart,uiSonEnd , uiSonEnd1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uiSonStart = itemView.findViewById(R.id.ui_son_start);
            uiSonEnd = itemView.findViewById(R.id.ui_son_end);
            uiSonEnd1 = itemView.findViewById(R.id.ui_son_end1);

            uiSonLin = itemView.findViewById(R.id.ui_son_lin);
            uiSonText1 = itemView.findViewById(R.id.ui_son_text1);
            uiSonText5 = itemView.findViewById(R.id.ui_son_text5);
            uiSonAddProject = itemView.findViewById(R.id.ui_son_addProject);
            uiSonText2 = itemView.findViewById(R.id.ui_son_text2);
            uiSonText3 = itemView.findViewById(R.id.ui_son_text3);
            uiSonText4 = itemView.findViewById(R.id.ui_son_text4);
            uiSonRecycler = itemView.findViewById(R.id.ui_son_recycler);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
