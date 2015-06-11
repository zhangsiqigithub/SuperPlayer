package com.dragon.superplayer.view.playerview;

import android.content.Context;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.dragon.superplayer.R;
import com.dragon.superplayer.callback.PlayControllerViewCallback;
import com.dragon.superplayer.interfaces.IPlayerControllerViewInterface;
import com.dragon.superplayer.interfaces.IPlayerViewInterface;
import com.dragon.superplayer.view.controllerview.DefaultPlayControllerView;
import com.dragon.superplayer.view.controllerview.DefaultPlayControllerView.PlayPauseBtnStatus;

/**
 * 整个播放界面的layout，仅供IViewController的实现类持有
 * @author yeguolong
 */
public class DefaultPlayerView implements IPlayerViewInterface {

    private Context mContext;// 上下文
    private RelativeLayout mPlayerView;// 这个播放界面的layout
    private SurfaceView mSurfaceView;// 播放显示的SurfaceView
    private RelativeLayout mGestureLayout;// 手势监听的布局
    private IPlayerControllerViewInterface mIPlayerControllerViewInterface;// 控制面板接口

    /**
     * 初始化播放整体布局
     * @param context
     *            上下文
     */
    public DefaultPlayerView(Context context) {
        this.init(context);
    }

    /**
     * 初始化播放整体布局
     * @param context
     *            上下文
     * @param iPlayerControllerViewInterface
     *            控制面板调用接口：目前只允许在构造中传入
     */
    public DefaultPlayerView(Context context,
            IPlayerControllerViewInterface iPlayerControllerViewInterface) {
        this.mIPlayerControllerViewInterface = iPlayerControllerViewInterface;
        this.init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context) {
        this.mContext = context;
        this.initView(context);
    }

    /**
     * 初始化各个View布局
     * @param context
     */
    private void initView(Context context) {
        this.mPlayerView = (RelativeLayout) View.inflate(context,
                R.layout.player_view, null);
        this.mPlayerView.setGravity(Gravity.CENTER);
        this.mSurfaceView = (SurfaceView) this.mPlayerView
                .findViewById(R.id.player_surfaceview);
        this.initPlayerControllerView(context);
    }

    @Override
    public void initGestureLayout() {
        this.mGestureLayout = (RelativeLayout) this.mPlayerView
                .findViewById(R.id.player_gesture_view);
        this.mGestureLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化控制面板
     * @param context
     */
    private void initPlayerControllerView(Context context) {
        /*
         * 1、如果之前没有初始化，那么就初始化默认的控制面板
         * 2、如果已经初始化了，那么就不做处理
         */
        if (this.mIPlayerControllerViewInterface == null) {
            this.mIPlayerControllerViewInterface = new DefaultPlayControllerView(
                    context);
        }
        this.mPlayerView.addView(this.mIPlayerControllerViewInterface
                .getPlayerControllerView());
    }

    @Override
    public SurfaceView getSurfaceView() {
        return this.mSurfaceView;
    }

    @Override
    public void showPlayerControllerView() {
        this.mIPlayerControllerViewInterface.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePlayerControllerView() {
        this.mIPlayerControllerViewInterface.setVisibility(View.GONE);
    }

    @Override
    public boolean isPlayerControllerViewShowing() {
        if (this.mIPlayerControllerViewInterface != null) {
            return this.mIPlayerControllerViewInterface.isShowing();
        }
        return false;
    }

    @Override
    public ViewGroup getPlayerView() {
        return this.mPlayerView;
    }

    @Override
    public void setPlayerControllerViewButtonClickListener(
            PlayControllerViewCallback playerControllerViewButtonClickListener) {
        if (this.mIPlayerControllerViewInterface != null) {
            this.mIPlayerControllerViewInterface
                    .setPlayerControllerViewButtonClickListener(playerControllerViewButtonClickListener);
        }
    }

    @Override
    public ViewGroup getGestureLayout() {
        return this.mGestureLayout;
    }

    @Override
    public void setPlayerSeekbarProgress(int currentPostion) {
        if (this.mIPlayerControllerViewInterface != null) {
            this.mIPlayerControllerViewInterface
                    .setPlayerSeekbarProgress(currentPostion);
        }
    }

    @Override
    public void setPlayerSeekbarMax(int max) {
        if (this.mIPlayerControllerViewInterface != null) {
            this.mIPlayerControllerViewInterface.setPlayerSeekbarMax(max);
        }
    }

    @Override
    public void setPlayerSeekBarChangeListener(OnSeekBarChangeListener l) {
        if (this.mIPlayerControllerViewInterface != null) {
            this.mIPlayerControllerViewInterface
                    .setPlayerSeekBarChangeListener(l);
        }

    }

    @Override
    public void updatePlayPauseBtn(PlayPauseBtnStatus playPauseBtnStatus) {
        if (this.mIPlayerControllerViewInterface != null) {
            this.mIPlayerControllerViewInterface
                    .updatePlayPauseBtn(playPauseBtnStatus);
        }
    }

}
