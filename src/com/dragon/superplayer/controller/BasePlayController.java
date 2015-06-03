package com.dragon.superplayer.controller;

import android.content.Context;
import android.text.TextUtils;

import com.dragon.superplayer.interfaces.IMediaPlayer;
import com.dragon.superplayer.interfaces.IPlayControler;
import com.dragon.superplayer.interfaces.IViewController;
import com.dragon.superplayer.mediaplayer.AudioFocusHelper;
import com.dragon.superplayer.mediaplayer.PlayStatus;
import com.dragon.superplayer.model.PlayItem;

/**
 * 播放控制实现基类：<br>
 * 1、持有最基本的播放器（IMediaPlayer）和音频焦点管理类（AudioFocusHelper）等。<br>
 * 2、播放控制实现类必须集成此类。<br>
 * @author yeguolong
 */
public abstract class BasePlayController implements IPlayControler {

    protected final Context mContext;
    protected PlayItem mPlayItem;// 播放条目
    protected IMediaPlayer mIMediaPlayer;
    protected AudioFocusHelper mAudioFocusHelper;// 音频焦点工具类

    public BasePlayController(Context context) {
        this.mContext = context;
        this.init();
    }

    private void init() {
        this.mAudioFocusHelper = new AudioFocusHelper(this.mContext);
    }

    protected abstract IMediaPlayer initMediaPlayer();

    @Override
    public void startPlay(PlayItem playItem) {
        if (playItem == null) {
            return;
        }
        this.mPlayItem = playItem;
        String playUrl = this.mPlayItem.getPlayUrl();
        if (TextUtils.isEmpty(playUrl)) {
            return;
        }
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.startPlay(playUrl);
        }
    }

    @Override
    public void startPlay(String playUrl, int startPosition) {
        if (TextUtils.isEmpty(playUrl)) {
            return;
        }
        this.mPlayItem = new PlayItem();
        this.mPlayItem.setPlayUrl(playUrl);
        this.mPlayItem.setStartPostion(startPosition);
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.startPlay(playUrl);
        }
    }

    @Override
    public void doPlay() {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.doPlay();
        }
    }

    @Override
    public void doPause() {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.doPause();
        }
    }

    @Override
    public void doStop() {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.doStop();
        }
    }

    @Override
    public void doSeek(int targetPosition) {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.doSeek(targetPosition);
        }
    }

    @Override
    public void gcMediaPlayer() {
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
    public IViewController getViewController() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void release() {
        // TODO Auto-generated method stub

    }

}
