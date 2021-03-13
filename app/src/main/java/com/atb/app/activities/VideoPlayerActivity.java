package com.atb.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;
import com.bumptech.glide.Glide;

import cn.jzvd.JzvdStd;

public class VideoPlayerActivity extends CommonActivity {
    private static final String EXTRA_VIDEO_URI = "video_uri";
    private TextureView textureView;

    private String videopath,thumb;
    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;
    private cn.jzvd.JzvdStd videoView;
    private ImageView iv_back;
    public static void start(Context context, String videoUrl, String thumb) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(EXTRA_VIDEO_URI, videoUrl);
        intent.putExtra("thumb",thumb);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_player);
        videopath = getIntent().getStringExtra(EXTRA_VIDEO_URI);
        thumb = getIntent().getStringExtra("thumb");
        setupVideoView();
    }

    private void setupVideoView() {
        // Make sure to use the correct VideoView import

        videoView = (JzvdStd) findViewById(R.id.video_view);
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //    videoView.setUp("https://jzvd.nathen.cn/video/460bad24-170c5bc6956-0007-1823-c86-de200.mp4", "饺子闭眼睛");
        videoView.setUp(videopath,"");
        Glide.with(this).load(thumb).into(videoView.posterImageView);

    }
    @Override
    public void onBackPressed() {
        if (videoView.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        videoView.releaseAllVideos();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}