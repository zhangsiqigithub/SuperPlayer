package com.dragon.superplayer.interfaces;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.dragon.superplayer.listener.PlayerControllerViewButtonClickListener;

/**
 * 布局控制接口，仅供IPlayControler的实现类持有
 * @author yeguolong
 */
public interface IViewController {

    /**
     * 获取SurfaceHolder，用于MediaPlayer的显示器
     * @return
     */
    public SurfaceHolder getSurfaceHolder();

    /**
     * 显示播放控制栏
     */
    public void showPlayerControllerView();

    /**
     * 隐藏播放控制栏
     */
    public void hidePlayerControllerView();

    /**
     * 获取播放界面布局
     * @return
     */
    public ViewGroup getPlayerView();

    /**
     * 获取用于显示的SurfaceView
     * @return
     */
    public SurfaceView getSurfaceView();

    /**
     * 改变SurfaceView宽高
     * @param width
     * @param height
     */
    public void changeSurfaceViewSize(int width, int height);

    /**
     * 设置控制面板点击事件监听
     * @param playerControllerViewButtonClickListener
     */
    public void setPlayerControllerViewButtonClickListener(
            PlayerControllerViewButtonClickListener playerControllerViewButtonClickListener);
}
