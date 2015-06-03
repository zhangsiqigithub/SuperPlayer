package com.dragon.superplayer.interfaces;

import android.view.ViewGroup;

import com.dragon.superplayer.listener.PlayerControllerViewButtonClickListener;

/**
 * 播放控制栏界面布局接口
 * @author yeguolong
 */
public interface IPlayerControllerViewInterface {

    /**
     * 获取播放控制栏界面layout
     */
    public ViewGroup getPlayerControllerView();

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
            PlayerControllerViewButtonClickListener playerControllerViewButtonClickListener);

}
