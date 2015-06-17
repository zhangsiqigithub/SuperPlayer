package com.dragon.superplayer.player.view.interfaces;

import android.view.ViewGroup;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.dragon.superplayer.player.view.DefaultPlayControllerView.PlayPauseBtnStatus;
import com.dragon.superplayer.player.view.callback.PlayControllerViewCallback;

/**
 * 播放控制栏界面布局接口
 * @author yeguolong
 */
public interface IPlayerControllerViewInterface {

    /**
     * 设置显示状态
     * @param visible
     */
    public void setVisibility(int visible);

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title);

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
    public boolean isShowing();

    /**
     * 设置播放进度
     */
    public void setPlayerSeekbarProgress(int currentPostion);

    /**
     * 获取播放进度条当前进度
     * @return
     */
    public int getPlayerSeekbarCurrentProgress();

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

    /**
     * 获取控制面板上边的布局
     * @return
     */
    public ViewGroup getPlayerControllerUpperView();

    /**
     * 获取控制面板下边的布局
     * @return
     */
    public ViewGroup getPlayerControllerBottomView();

}
