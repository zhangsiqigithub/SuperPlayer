package com.dragon.superplayer;

import android.app.Activity;
import android.os.Bundle;

import com.dragon.superplayer.player.MusicPlayerManager;
import com.dragon.superplayer.player.util.LogUtil;

public class MusicTestActivity extends Activity {

    private MusicPlayerManager mMusicPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mMusicPlayerManager = new MusicPlayerManager(this);

        String url = "http://m5.file.xiami.com/135/135/420304/1769962750_2050446_l.mp3?auth_key=bad5e6c45d84f82de3cec2110efe11b2-1432771200-0-null";
        // String url =
        // "http://m5.file.xiami.com/198/1198/6092/74560_10709259_l.mp3?auth_key=ff452dd093934813f201b43c8e2c1722-1432771200-0-null";
        this.mMusicPlayerManager.getPlayControler().startPlay(url, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mMusicPlayerManager != null) {
            this.mMusicPlayerManager.release();
        }
    }

}
