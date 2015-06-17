package com.dragon.superplayer.player.view.interfaces;

import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.dragon.superplayer.player.callback.PlayControllerViewCallback;
import com.dragon.superplayer.player.view.DefaultPlayControllerView;
import com.dragon.superplayer.player.view.DefaultPlayControllerView.PlayPauseBtnStatus;

/**
 * 播放整个界面布局调用接口
 * @author yeguolong
 */
public interface IPlayerViewInterface {

    /**
     * 获取用于显示的SurfaceView
     * @return
     */
    public SurfaceView getSurfaceView();

    /**
     * 初始化手势布局
     */
    public void initGestureLayout();

    /**
     * 获取手势监听布局
     */
    public ViewGroup getGestureLayout();

    /**
     * 获取播放界面layout
     */
    public ViewGroup getPlayerView();

    /**
     * 设置控制面板点击事件监听
     * @param playerControllerViewButtonClickListener
     */
    public void setPlayerControllerViewButtonClickListener(
            PlayControllerViewCallback playerControllerViewButtonClickListener);

    /**
     * 控制面板是否显示
     * @return
     */
    public boolean isPlayerControllerViewShowing();

    /**
     * 设置播放进度
     */
    public void setPlayerSeekbarProgress(int currentPostion);

    /**
     * 设置播放进度条的总时长
     */
    public void setPlayerSeekbarMax(int max);

    /**
     * 设置播放进度条监听回调
     * @param l
     */
    public void setPlayerSeekBarChangeListener(OnSeekBarChangeListener l);

    /**
     * 更新播放暂停按钮的图标
     * @param isPlaying
     */
    public void updatePlayPauseBtn(PlayPauseBtnStatus playPauseBtnStatus);

}
