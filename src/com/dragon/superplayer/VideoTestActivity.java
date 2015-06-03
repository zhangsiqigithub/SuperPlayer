package com.dragon.superplayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dragon.superplayer.manager.VideoPlayerManager;

public class VideoTestActivity extends Activity {

    private VideoPlayerManager mVideoPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.init();
    }

    private void init() {
        this.mVideoPlayerManager = new VideoPlayerManager(this);
        RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.layout);
        ViewGroup playerView = this.mVideoPlayerManager.getViewController()
                .getPlayerView();
        layout.addView(playerView);
        this.testPlay();
    }

    /**
     * 播放测试
     */
    private void testPlay() {
        this.copyToSdcard();
        String url = this.DATABASE_PATH + "/" + this.DATABASE_FILENAME;
        this.mVideoPlayerManager.getPlayController().startPlay(url, 0);
    }

    private final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath();
    private final String DATABASE_FILENAME = "test.mp4";

    private void copyToSdcard() {
        String databaseFilename = this.DATABASE_PATH + "/"
                + this.DATABASE_FILENAME;
        File dir = new File(this.DATABASE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!(new File(databaseFilename)).exists()) {
            try {
                InputStream is = this.getResources()
                        .openRawResource(R.raw.test);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;

                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mVideoPlayerManager != null) {
            this.mVideoPlayerManager.onScreenOrientationChanged(newConfig);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mVideoPlayerManager != null) {
            this.mVideoPlayerManager.getPlayController().doPlay();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.mVideoPlayerManager != null) {
            this.mVideoPlayerManager.getPlayController().doPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mVideoPlayerManager != null) {
            this.mVideoPlayerManager.release();
        }
    }

}
