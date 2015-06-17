package com.dragon.superplayer.player.controller.viewcontroller;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.dragon.superplayer.player.callback.PlayControllerViewCallback;
import com.dragon.superplayer.player.controller.viewcontroller.interfaces.IViewController;
import com.dragon.superplayer.player.displayer.DefaultControllerViewDisplayer;
import com.dragon.superplayer.player.displayer.interfaces.IControllerViewDisplayer;
import com.dragon.superplayer.player.gesture.GestureCallback;
import com.dragon.superplayer.player.gesture.GestureManager;
import com.dragon.superplayer.player.view.DefaultPlayControllerView;
import com.dragon.superplayer.player.view.DefaultPlayControllerView.PlayPauseBtnStatus;
import com.dragon.superplayer.player.view.DefaultPlayerTipsFloatView;
import com.dragon.superplayer.player.view.DefaultPlayerView;
import com.dragon.superplayer.player.view.interfaces.IPlayerControllerViewInterface;
import com.dragon.superplayer.player.view.interfaces.IPlayerTipsFloatViewInterface;
import com.dragon.superplayer.player.view.interfaces.IPlayerViewInterface;

/**
 * 界面布局控制实现类：<br>
 * 持有整个界面的布局管理权限，即：IPlayerViewInterface
 * @author yeguolong
 */
public class ViewController implements IViewController {

    private final Context mContext;
    private IPlayerViewInterface mIPlayerViewInterface;
    private IPlayerControllerViewInterface mIPlayerControllerViewInterface;// 控制面板接口
    private IPlayerTipsFloatViewInterface mIPlayerTipsFloatViewInterface;// 提示界面接口
    private IControllerViewDisplayer mIControllerViewDisplayer;
    private GestureManager mGestureManager;

    private SurfaceView mSurfaceView;

    public ViewController(Context context) {
        this.mContext = context;
        this.init();
    }

    private void init() {
        this.initView();
        this.initGesture();
        this.initControllerViewDisplayer();
    }

    private void initView() {
        // 初始化控制面板实现类
        this.mIPlayerControllerViewInterface = new DefaultPlayControllerView(
                this.mContext);
        // 初始化提示界面实现类
        this.mIPlayerTipsFloatViewInterface = new DefaultPlayerTipsFloatView(
                this.mContext);
        // 初始化PlayerView
        this.mIPlayerViewInterface = new DefaultPlayerView(this.mContext,
                this.mIPlayerControllerViewInterface,
                this.mIPlayerTipsFloatViewInterface);
        // 得到SurfaceView
        this.mSurfaceView = this.mIPlayerViewInterface.getSurfaceView();
    }

    private void initGesture() {
        if (this.mIPlayerViewInterface != null) {
            this.mIPlayerViewInterface.initGestureLayout();
            ViewGroup gestureLayout = this.mIPlayerViewInterface
                    .getGestureLayout();
            if (gestureLayout != null) {
                this.mGestureManager = new GestureManager(gestureLayout);
            }
        }
    }

    private void initControllerViewDisplayer() {
        this.mIControllerViewDisplayer = new DefaultControllerViewDisplayer();
    }

    @Override
    public SurfaceHolder getSurfaceHolder() {
        return this.mSurfaceView.getHolder();
    }

    @Override
    public void showPlayerControllerView(boolean useAnimation) {
        if (this.mIControllerViewDisplayer != null) {
            this.mIControllerViewDisplayer.displayControllerView(
                    this.mIPlayerControllerViewInterface, true, useAnimation);
        }
    }

    @Override
    public void hidePlayerControllerView() {
        if (this.mIControllerViewDisplayer != null) {
            this.mIControllerViewDisplayer.displayControllerView(
                    this.mIPlayerControllerViewInterface, false, true);
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
        if (this.mIPlayerControllerViewInterface != null) {
            return this.mIPlayerControllerViewInterface
                    .getPlayerControllerUpperView().getVisibility() == View.VISIBLE;
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
    public int getPlayerSeekbarCurrentProgress() {
        if(mIPlayerControllerViewInterface != null){
            return mIPlayerControllerViewInterface.getPlayerSeekbarCurrentProgress();
        }
        return 0;
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

    @Override
    public void showLoading(String loadingText) {
        if (this.mIPlayerTipsFloatViewInterface != null) {
            this.mIPlayerTipsFloatViewInterface.showLoading(loadingText);
        }
    }

    @Override
    public void hideLoading() {
        if (this.mIPlayerTipsFloatViewInterface != null) {
            this.mIPlayerTipsFloatViewInterface.hideLoading();
        }
    }

    @Override
    public void showBrightness(int value) {
        if (this.mIPlayerTipsFloatViewInterface != null) {
            this.mIPlayerTipsFloatViewInterface.showBrightness(value);
        }
    }

    @Override
    public void hideBrightness() {
        if (this.mIPlayerTipsFloatViewInterface != null) {
            this.mIPlayerTipsFloatViewInterface.hideBrightness();
        }
    }

    @Override
    public void showVolume(int value) {
        if (this.mIPlayerTipsFloatViewInterface != null) {
            this.mIPlayerTipsFloatViewInterface.showVolume(value);
        }
    }

    @Override
    public void hideVolume() {
        if (this.mIPlayerTipsFloatViewInterface != null) {
            this.mIPlayerTipsFloatViewInterface.hideVolume();
        }
    }
}
