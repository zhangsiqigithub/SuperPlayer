package com.dragon.superplayer.controller;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dragon.superplayer.interfaces.IPlayerViewInterface;
import com.dragon.superplayer.interfaces.IViewController;
import com.dragon.superplayer.listener.PlayerControllerViewButtonClickListener;
import com.dragon.superplayer.view.PlayerView;

/**
 * 界面布局控制实现类：<br>
 * 持有整个界面的布局管理权限，即：IPlayerViewInterface
 * @author yeguolong
 */
public class ViewController implements IViewController {

    private final Context mContext;
    private IPlayerViewInterface mIPlayerViewInterface;

    private SurfaceHolder mSurfaceHolder;

    public ViewController(Context context) {
        this.mContext = context;
        this.init();
    }

    private void init() {
        this.mIPlayerViewInterface = new PlayerView(this.mContext);
        SurfaceView surfaceView = this.mIPlayerViewInterface.getSurfaceView();
        this.mSurfaceHolder = surfaceView.getHolder();
    }

    @Override
    public SurfaceHolder getSurfaceHolder() {
        return this.mSurfaceHolder;
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
    public void setPlayerControllerViewButtonClickListener(
            PlayerControllerViewButtonClickListener playerControllerViewButtonClickListener) {
        if (this.mIPlayerViewInterface != null) {
            this.mIPlayerViewInterface
                    .setPlayerControllerViewButtonClickListener(playerControllerViewButtonClickListener);
        }
    }
}
