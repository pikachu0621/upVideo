/**
 * 压缩上传   视频,支持断点续传
 * <p>
 * 传入List<VideoUpJson>
 */
package com.pikachu.upvideo.activity.up;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.iceteck.silicompressorr.SiliCompressor;
import com.pikachu.upvideo.R;
import com.pikachu.upvideo.cls.VideoUpJson;
import com.pikachu.upvideo.util.AppInfo;
import com.pikachu.upvideo.util.base.BaseActivity;
import com.pikachu.upvideo.util.tools.ToolAddProjects;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class UpZipVideoActivity extends BaseActivity implements ServiceConnection {

    private List<VideoUpJson> projectList;
    private Toolbar barToolbar;
    private RecyclerView activityUpZipRecycler;
    private TextView testText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_zip_video, R.id.bar_view);
        initView();
        init();

    }


    private void init() {

        VideoUpJson[] projects = getSerializableExtra(AppInfo.START_ACTIVITY_KEY_1, VideoUpJson[].class);
        if (projects == null || projects.length <= 0) {
            showToast("无项目");
            finish();
            return;
        }
        projectList = Arrays.asList(projects);
        setSupportActionBar(barToolbar);
        setHead(true, "项目上传", "项目没有完成时，请勿清理软件后台", this::finish);


      /*  Intent intent = new Intent(this, UpZipService.class);
        bindService(intent,this, Context.BIND_AUTO_CREATE);*/


        VideoUpJson videoUpJson = projectList.get(0);
        List<String> projectVideoPath = ToolAddProjects.getProjectVideoPath(this, videoUpJson);
        if (projectVideoPath == null || projectVideoPath.size() <= 0) {
            showToast("没有视频");
            return;
        }

        String s = projectVideoPath.get(0);
        showToast(s);


        // new VideoCompressAsyncTask(this.getApplicationContext()).execute(projectVideoPath.get(0), projectVideoPath.get(0));

        new Thread(() -> {

            try {
                long start = System.currentTimeMillis();

                String s1 = SiliCompressor.with(UpZipVideoActivity.this).compressVideo(projectVideoPath.get(0), projectVideoPath.get(0));

                long end = System.currentTimeMillis();
                long diff = (end - start) / 1000;

                runOnUiThread(() -> {
                            String format = String.format("未压缩前文件大小：%s MB\n压缩后：%s MB\n耗时：%sS\n保存到：%s",
                                    ToolAddProjects.getFileSize(projectVideoPath.get(0), ToolAddProjects.Type.MB),
                                    ToolAddProjects.getFileSize(s1, ToolAddProjects.Type.MB),
                                    diff, s1);
                            showToast(format);
                            testText.setText(format);
                        }
                );


            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }).start();


    }


    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {

                //This bellow is just a temporary solution to test that method call works
                boolean b = Boolean.parseBoolean(paths[0]);
                if (b) {
                    filePath = SiliCompressor.with(mContext).compressVideo(paths[1], paths[2]);
                }


            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);

         /*   File imageFile = new File(compressedFilePath);
            compressUri = Uri.fromFile(imageFile);
            String name = imageFile.getName();
            float length = imageFile.length() / 1024f; // Size in KB

            String value = length + " KB";
            String text = String.format(Locale.US, "%s\nName: %s\nSize: %s", getString(R.string.video_compression_complete), name, value);
            compressionMsg.setVisibility(View.GONE);
            picDescription.setVisibility(View.VISIBLE);
            picDescription.setText(text);
            Log.i("Silicompressor", "Path: " + compressedFilePath);*/
        }
    }


    private void initView() {
        barToolbar = findViewById(R.id.bar_toolbar);
        activityUpZipRecycler = findViewById(R.id.activity_up_zip_recycler);


        testText = findViewById(R.id.test_text);
    }


    ////////////////////////// 服务 ///////////////////////////////////////////////

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        //连接时
        UpZipService.ConnectBinder binder = (UpZipService.ConnectBinder) service;


        VideoUpJson videoUpJson = projectList.get(0);
        List<String> projectVideoPath = ToolAddProjects.getProjectVideoPath(this, videoUpJson);
        if (projectVideoPath == null || projectVideoPath.size() <= 0) {
            showToast("没有视频");
            return;
        }
        showToast(projectVideoPath.get(0));

        String s = binder.startZipVideo(projectVideoPath.get(0), projectVideoPath.get(0));
        showToast(s);


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        //断开时
    }


}