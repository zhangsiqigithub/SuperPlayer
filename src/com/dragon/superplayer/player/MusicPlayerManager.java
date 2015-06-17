package com.dragon.superplayer.player;

import android.app.Activity;

import com.dragon.superplayer.player.controller.playcontroller.MusicPlayController;
import com.dragon.superplayer.player.controller.playcontroller.interfaces.IPlayControler;
import com.dragon.superplayer.player.util.LogUtil;

/**
 * 音乐模块管理者：<br>
 * 目前还没有完善这个模块儿，只提供了基本播放功能。
 * @author yeguolong
 */
public class MusicPlayerManager {

    private Activity mContext;
    private IPlayControler mIPlayControler;

    public MusicPlayerManager(Activity context) {
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
