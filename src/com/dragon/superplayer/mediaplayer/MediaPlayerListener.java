package com.dragon.superplayer.mediaplayer;

import android.media.MediaPlayer;

public interface MediaPlayerListener {

    /**
     * 播放信息回调
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    public boolean onInfo(MediaPlayer mp, int what, int extra);

    /**
     * 播放完成
     * @param mp
     */
    public void onCompletion(MediaPlayer mp);

    /**
     * 播放错误
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    public boolean onError(MediaPlayer mp, int what, int extra);

    /**
     * 请求完成，准备播放
     * @param mp
     */
    public void onPrepared(MediaPlayer mp);

    /**
     * 当视频大小发生改变
     * @param mp
     * @param width
     * @param height
     */
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height);

    /**
     * 缓冲开始
     * @param mp
     * @param extra
     */
    public void onBufferingStart(MediaPlayer mp, int extra);

    /**
     * 缓冲结束
     * @param mp
     * @param extra
     */
    public void onBufferingEnd(MediaPlayer mp, int extra);

    /**
     * 播放开始，视频第一帧
     * @param mp
     * @param extra
     */
    public void onVideoRenderingStart(MediaPlayer mp, int extra);
}
