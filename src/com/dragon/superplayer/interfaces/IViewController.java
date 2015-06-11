package com.dragon.superplayer.interfaces;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.dragon.superplayer.callback.PlayControllerViewCallback;
import com.dragon.superplayer.gesture.GestureCallback;
import com.dragon.superplayer.view.controllerview.DefaultPlayControllerView.PlayPauseBtnStatus;

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
     * 控制面板是否显示
     * @return
     */
    public boolean isPlayerControllerViewShowing();

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
    public void setPlayerControllerViewCallbackListener(
            PlayControllerViewCallback playerControllerViewButtonClickListener);

    /**
     * 设置手势回调
     * @param gestureCallback
     */
    public void setGestureCallBack(GestureCallback gestureCallback);

    /**
     * 设置播放进度
     */
    public void setPlayerSeekbarProgress(int currentPostion);

    /**
     * 设置播放进度
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
