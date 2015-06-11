package com.dragon.superplayer.interfaces;

import com.dragon.superplayer.callback.PlayControllerCallback;
import com.dragon.superplayer.mediaplayer.PlayStatus;
import com.dragon.superplayer.model.PlayItem;

/**
 * 播放控制接口：仅供PlayerManager或外部使用者调用
 * @author yeguolong
 */
public interface IPlayControler {

    /**
     * 开启一次播放：Url播放
     * @param url
     *            播放地址
     */
    public void startPlay(String playUrl, int startPosition);

    /**
     * 开启一次播放：PlayItem播放
     * @param playItem
     */
    public void startPlay(PlayItem playItem);

    /**
     * 播放
     */
    public void doPlay();

    /**
     * 暂停
     */
    public void doPause();

    /**
     * 停止
     */
    public void doStop();

    /**
     * seek快进
     * @param targetPosition
     *            毫秒值
     */
    public void doSeek(int targetPosition);

    /**
     * 销毁播放器
     */
    public void gcMediaPlayer();

    /**
     * 获取播放状态
     */
    public PlayStatus getPlayStatus();

    /**
     * 获取IViewController
     */
    public IViewController getViewController();

    /**
     * 释放播放器
     */
    public void release();

    /**
     * 设置播放控制回调接口
     * @param playControllerCallback
     */
    public void setPlayControllerCallback(
            PlayControllerCallback playControllerCallback);

    /**
     * 请求音频焦点
     */
    public void requestAudioFocus();

    /**
     * 释放音频焦点
     */
    public void releaseAudioFocus();

    public int getCurrentPosition();

    public int getDuration();

}
