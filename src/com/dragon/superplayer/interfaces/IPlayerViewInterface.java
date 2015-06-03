package com.dragon.superplayer.interfaces;

import android.view.SurfaceView;
import android.view.ViewGroup;

import com.dragon.superplayer.listener.PlayerControllerViewButtonClickListener;

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
     * 显示播放控制栏
     */
    public void showPlayerControllerView();

    /**
     * 隐藏播放控制栏
     */
    public void hidePlayerControllerView();

    /**
     * 获取播放界面layout
     */
    public ViewGroup getPlayerView();

    /**
     * 设置控制面板点击事件监听
     * @param playerControllerViewButtonClickListener
     */
    public void setPlayerControllerViewButtonClickListener(
            PlayerControllerViewButtonClickListener playerControllerViewButtonClickListener);

}
