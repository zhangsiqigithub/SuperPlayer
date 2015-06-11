package com.dragon.superplayer;

import android.content.Context;

import com.dragon.superplayer.controller.MusicPlayController;
import com.dragon.superplayer.interfaces.IPlayControler;
import com.dragon.superplayer.util.LogUtil;

public class MusicPlayerManager {

    private Context mContext;
    private IPlayControler mIPlayControler;

    public MusicPlayerManager(Context context) {
        this.log("MusicPlayerManager-->context:" + context);
        if (context == null) {
            return;
        }
        this.mContext = context;
        this.init();
    }

    private void init() {
        this.mIPlayControler = new MusicPlayController(this.mContext);
    }

    public IPlayControler getPlayControler() {
        return this.mIPlayControler;
    }

    public void release() {
        if (this.mIPlayControler != null) {
            this.mIPlayControler.gcMediaPlayer();
        }
    }

    private void log(String log) {
        LogUtil.d("MusicPlayerManager-->" + log);
    }

}
