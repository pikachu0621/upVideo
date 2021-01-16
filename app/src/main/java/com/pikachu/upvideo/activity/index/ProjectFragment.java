package com.pikachu.upvideo.activity.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.init.AddProjects;

import java.util.List;

public class ProjectFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private View inflate;
    private SwipeRefreshLayout proSw;
    private RecyclerView proRe;
    private FragmentActivity activity;
    private AddProjects addProject;
    private List<VideoUpJson> videoUpJsons;
    private RecyclerAdapter recyclerAdapter;

    public ProjectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.fragment_project, container, false);
        activity = getActivity();
        initView();
        init();
        return inflate;
    }

    private void init() {




        //读取项目
        addProject = AddProjects.getAddProject(activity);
        //项目显示
        videoUpJsons = addProject.readProject();

        proRe.setLayoutManager(new LinearLayoutManager(activity));
        recyclerAdapter = new RecyclerAdapter(videoUpJsons, getFragmentManager());
        proRe.setAdapter(recyclerAdapter);
        proSw.setOnRefreshListener(this);
    }

    private void initView() {
        proSw =  inflate.findViewById(R.id.pro_sw);
        proRe =  inflate.findViewById(R.id.pro_re);
    }



    //刷新监听
    @Override
    public void onRefresh() {
        refresh();
    }



    //刷新数据
    public void  refresh(){
        videoUpJsons = addProject.readProject();
        recyclerAdapter.reData(videoUpJsons);
        proSw.setRefreshing(false);
    }

}