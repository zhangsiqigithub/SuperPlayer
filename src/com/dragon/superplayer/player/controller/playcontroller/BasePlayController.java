package com.dragon.superplayer.player.controller.playcontroller;

import android.app.Activity;

import com.dragon.superplayer.player.controller.playcontroller.interfaces.IPlayControler;
import com.dragon.superplayer.player.mediaplayer.PlayStatus;
import com.dragon.superplayer.player.mediaplayer.interfaces.IMediaPlayer;
import com.dragon.superplayer.player.model.PlayItem;
import com.dragon.superplayer.player.util.AudioFocusHelper;

/**
 * 播放控制实现基类（抽象类）：<br>
 * 1、持有最基本的播放器（IMediaPlayer）和音频焦点管理类（AudioFocusHelper）等。<br>
 * 2、播放控制实现类必须集成此类。<br>
 * @author yeguolong
 */
public abstract class BasePlayController implements IPlayControler {

    protected final Activity mContext;
    protected PlayItem mPlayItem;// 播放条目
    protected IMediaPlayer mIMediaPlayer;
    private int mLastSeekPosition = 0;
    protected AudioFocusHelper mAudioFocusHelper;// 音频焦点工具类

    public BasePlayController(Activity context) {
        this.mContext = context;
        this.init();
    }

    private void init() {
        this.mAudioFocusHelper = new AudioFocusHelper(this.mContext);
    }

    /**
     * 初始化播放器：抽象接口。<br>
     * 这里放在子类去初始化，本基类中不做处理。
     * @return
     */
    protected abstract IMediaPlayer initMediaPlayer();

    @Override
    public synchronized void gcMediaPlayer() {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.gcMediaPlayer();
        }
    }

    @Override
    public PlayStatus getPlayStatus() {
        if (this.mIMediaPlayer != null) {
            return this.mIMediaPlayer.getPlayStatus();
        }
        return PlayStatus.STOPED;
    }

    @Override
    public void requestAudioFocus() {
        if (this.mAudioFocusHelper != null) {
            this.mAudioFocusHelper.requestFocus();
        }
    }

    @Override
    public void releaseAudioFocus() {
        if (this.mAudioFocusHelper != null) {
            this.mAudioFocusHelper.abandonFocus();
        }
    }

    /**
     * 设置
     * @param lastSeekPosition
     */
    protected void setLastSeekPosition(int lastSeekPosition) {
        this.mLastSeekPosition = lastSeekPosition;
    }

}
