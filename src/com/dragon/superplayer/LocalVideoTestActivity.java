package com.dragon.superplayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dragon.superplayer.manager.VideoPlayerManager;
import com.dragon.superplayer.util.LogUtil;

public class LocalVideoTestActivity extends Activity {

    private VideoPlayerManager mVideoPlayerManager;

    private String mPlayUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.handleIntent();
        this.init();
    }

    private void handleIntent() {
        String action = this.getIntent().getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = this.getIntent().getData();
            LogUtil.d("uri:" + uri);

            if (uri != null) {
                String scheme = uri.getScheme();
                LogUtil.d("scheme:" + scheme);
                if ("content".equals(scheme)) {
                    Cursor cursor = this.getContentResolver().query(uri,
                            new String[] { MediaStore.Video.Media.DATA }, null,
                            null, null);
                    if (cursor != null) {
                        int index = cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                        if (cursor.moveToFirst()) {
                            this.mPlayUrl = cursor.getString(index);
                            return;
                        }
                    }
                } else if ("file".equals(scheme)) {
                    this.mPlayUrl = uri.getPath();
                    return;
                } else {
                    this.mPlayUrl = uri.toString();
                    return;
                }
            }
        }
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
        if (TextUtils.isEmpty(this.mPlayUrl)) {
            this.copyToSdcard();
            this.mPlayUrl = this.DATABASE_PATH + "/" + this.DATABASE_FILENAME;
        }
        this.mVideoPlayerManager.getPlayController().startPlay(this.mPlayUrl);
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
                InputStream is = this.getResources().openRawResource(
                        R.raw.test);
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
