package com.dragon.superplayer.controller;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.dragon.superplayer.callback.PlayControllerViewCallback;
import com.dragon.superplayer.gesture.GestureCallback;
import com.dragon.superplayer.gesture.GestureManager;
import com.dragon.superplayer.interfaces.IPlayerViewInterface;
import com.dragon.superplayer.interfaces.IViewController;
import com.dragon.superplayer.view.controllerview.DefaultPlayControllerView.PlayPauseBtnStatus;
import com.dragon.superplayer.view.playerview.DefaultPlayerView;

/**
 * 界面布局控制实现类：<br>
 * 持有整个界面的布局管理权限，即：IPlayerViewInterface
 * @author yeguolong
 */
public class ViewController implements IViewController {

    private final Context mContext;
    private IPlayerViewInterface mIPlayerViewInterface;
    private GestureManager mGestureManager;

    private SurfaceView mSurfaceView;

    public ViewController(Context context) {
        this.mContext = context;
        this.init();
    }

    private void init() {
        this.initView();
        this.initGesture();
    }

    private void initView() {
        // 初始化PlayerView
        this.mIPlayerViewInterface = new DefaultPlayerView(this.mContext);
        // 使用手势滑动
        this.mIPlayerViewInterface.initGestureLayout();
        // 得到SurfaceView
        this.mSurfaceView = this.mIPlayerViewInterface.getSurfaceView();
    }

    private void initGesture() {
        if (this.mIPlayerViewInterface != null) {
            ViewGroup gestureLayout = this.mIPlayerViewInterface
                    .getGestureLayout();
            if (gestureLayout != null) {
                this.mGestureManager = new GestureManager(gestureLayout);
            }
        }
    }

    @Override
    public SurfaceHolder getSurfaceHolder() {
        return this.mSurfaceView.getHolder();
    }

    @Override
    public void showPlayerControllerView() {
        if (this.mIPlayerViewInterface != null) {
            this.mIPlayerViewInterface.showPlayerControllerView();
        }
    }

    @Override
    public void hidePlayerControllerView() {
        if (this.mIPlayerViewInterface != null) {
            this.mIPlayerViewInterface.hidePlayerControllerView();
        }
    }

    @Override
    public ViewGroup getPlayerView() {
        return this.mIPlayerViewInterface.getPlayerView();
    }

    @Override
    public void changeSurfaceViewSize(int width, int height) {
        this.mIPlayerViewInterface.getSurfaceView().setLayoutParams(
                new RelativeLayout.LayoutParams(width, height));
    }

    @Override
    public SurfaceView getSurfaceView() {
        return this.mIPlayerViewInterface.getSurfaceView();
    }

    @Override
    public void setPlayerControllerViewCallbackListener(
            PlayControllerViewCallback playerControllerViewButtonClickListener) {
        if (this.mIPlayerViewInterface != null) {
            this.mIPlayerViewInterface
                    .setPlayerControllerViewButtonClickListener(playerControllerViewButtonClickListener);
        }
    }

    @Override
    public void setGestureCallBack(GestureCallback gestureCallback) {
        if (this.mGestureManager != null) {
            this.mGestureManager.setGestureCallBack(gestureCallback);
        }
    }

    @Override
    public boolean isPlayerControllerViewShowing() {
        if (this.mIPlayerViewInterface != null) {
            return this.mIPlayerViewInterface.isPlayerControllerViewShowing();
        }
        return false;
    }

    @Override
    public void setPlayerSeekbarProgress(int currentPostion) {
        if (this.mIPlayerViewInterface != null) {
            this.mIPlayerViewInterface.setPlayerSeekbarProgress(currentPostion);
        }
    }

    @Override
    public void setPlayerSeekbarMax(int max) {
        if (this.mIPlayerViewInterface != null) {
            this.mIPlayerViewInterface.setPlayerSeekbarMax(max);
        }
    }

    @Override
    public void setPlayerSeekBarChangeListener(OnSeekBarChangeListener l) {
        if (this.mIPlayerViewInterface != null) {
            this.mIPlayerViewInterface.setPlayerSeekBarChangeListener(l);
        }
    }

    @Override
    public void updatePlayPauseBtn(PlayPauseBtnStatus playPauseBtnStatus) {
        if (this.mIPlayerViewInterface != null) {
            this.mIPlayerViewInterface.updatePlayPauseBtn(playPauseBtnStatus);
        }

    }
}
