package com.dragon.superplayer.interfaces;

import com.dragon.superplayer.mediaplayer.MediaPlayerListener;
import com.dragon.superplayer.mediaplayer.PlayStatus;

import android.view.SurfaceHolder;


/**
 * MediaPlayer播放控制接口，仅供IPlayControler的实现类持有
 * @author yeguolong
 */
public interface IMediaPlayer {

    /**
     * 开启一次播放
     * @param url
     *            播放地址
     */
    public void startPlay(String url);

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
     */
    public void doSeek(int targetPosition);

    /**
     * 销毁播放器
     */
    public void gcMediaPlayer();

    /**
     * 设置播放回调接口
     */
    public void setMediaPlayerListener(MediaPlayerListener mediaPlayerListener);

    /**
     * 设置视频资源的显示器
     */
    public void setDisPlay(SurfaceHolder surfaceHolder);

    /**
     * 获取播放状态
     */
    public PlayStatus getPlayStatus();

    /**
     * 获取当前播放进度
     * @return
     */
    public int getCurrentPosition();

    /**
     * 获取播放时长
     * @return
     */
    public int getDuration();

}
