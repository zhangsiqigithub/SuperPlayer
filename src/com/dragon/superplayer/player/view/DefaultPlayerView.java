package com.dragon.superplayer.player.view;

import android.content.Context;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.dragon.superplayer.R;
import com.dragon.superplayer.player.view.DefaultPlayControllerView.PlayPauseBtnStatus;
import com.dragon.superplayer.player.view.callback.PlayControllerViewCallback;
import com.dragon.superplayer.player.view.interfaces.IPlayerControllerViewInterface;
import com.dragon.superplayer.player.view.interfaces.IPlayerTipsFloatViewInterface;
import com.dragon.superplayer.player.view.interfaces.IPlayerViewInterface;

/**
 * 整个播放界面的layout，仅供IViewController的实现类持有
 * @author yeguolong
 */
public class DefaultPlayerView implements IPlayerViewInterface {

    private static final int INDEX_SURFACEVIEW = 0;
    private static final int INDEX_TIPS_FLOAT_LAYOUT = 1;
    private static final int INDEX_GESTURE = 2;
    private static final int INDEX_CONTROLLER_UPPER_VIEW = 3;
    private static final int INDEX_CONTROLLER_BOTTOM_VIEW = 4;

    private Context mContext;// 上下文
    private RelativeLayout mPlayerView;// 这个播放界面的layout
    private SurfaceView mSurfaceView;// 播放显示的SurfaceView
    private RelativeLayout mGestureLayout;// 手势监听的布局
    private final IPlayerControllerViewInterface mIPlayerControllerViewInterface;// 控制面板接口
    private final IPlayerTipsFloatViewInterface mIPlayerTipsFloatViewInterface;// 手势监听的布局

    /**
     * 初始化播放整体布局
     * @param context
     *            上下文
     * @param iPlayerControllerViewInterface
     *            控制面板调用接口：目前只允许在构造中传入
     */
    public DefaultPlayerView(Context context,
            IPlayerControllerViewInterface iPlayerControllerViewInterface,
            IPlayerTipsFloatViewInterface iPlayerTipsFloatViewInterface) {
        this.mIPlayerControllerViewInterface = iPlayerControllerViewInterface;
        this.mIPlayerTipsFloatViewInterface = iPlayerTipsFloatViewInterface;
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
        this.mPlayerView = new RelativeLayout(context);
        this.mPlayerView.setGravity(Gravity.CENTER);
        this.initSurfaceView(context);
        this.initTipsFloatLayout();
        this.initGestureLayout();
        this.initPlayerControllerView(context);
    }

    private void initSurfaceView(Context context) {
        this.mSurfaceView = new SurfaceView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        this.mSurfaceView.setLayoutParams(params);
        this.mPlayerView.addView(this.mSurfaceView, INDEX_SURFACEVIEW);
    }

    private void initTipsFloatLayout() {
        if (this.mIPlayerTipsFloatViewInterface != null) {
            ViewGroup playerTipsFloatView = this.mIPlayerTipsFloatViewInterface
                    .getPlayerTipsFloatView();
            if (playerTipsFloatView != null) {
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                playerTipsFloatView.setLayoutParams(params);
                this.mPlayerView.addView(playerTipsFloatView,
                        INDEX_TIPS_FLOAT_LAYOUT);
            }
        }
    }

    /**
     * 初始化手势监听布局
     * @param context
     */
    @Override
    public void initGestureLayout() {
        this.mGestureLayout = new RelativeLayout(this.mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        this.mGestureLayout.setLayoutParams(params);
        this.mPlayerView.addView(this.mGestureLayout, INDEX_GESTURE);
    }

    /**
     * 初始化控制面板
     * @param context
     */
    private void initPlayerControllerView(Context context) {
        if (this.mIPlayerControllerViewInterface != null) {
            RelativeLayout playerControllerUpperView = (RelativeLayout) this.mIPlayerControllerViewInterface
                    .getPlayerControllerUpperView();
            RelativeLayout playerControllerBottomView = (RelativeLayout) this.mIPlayerControllerViewInterface
                    .getPlayerControllerBottomView();
            if (playerControllerUpperView != null) {
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, playerControllerUpperView
                                .getResources().getDimensionPixelSize(
                                        R.dimen.player_controller_view_height));
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                playerControllerUpperView.setLayoutParams(params);
                playerControllerUpperView
                        .setBackgroundColor(playerControllerUpperView
                                .getResources().getColor(
                                        R.color.player_color_88000000));
                playerControllerUpperView.setGravity(Gravity.CENTER_VERTICAL);
                playerControllerUpperView.setVisibility(View.GONE);

                this.mPlayerView.addView(playerControllerUpperView,
                        INDEX_CONTROLLER_UPPER_VIEW);
            }
            if (playerControllerBottomView != null) {
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, playerControllerBottomView
                                .getResources().getDimensionPixelSize(
                                        R.dimen.player_controller_view_height));
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                playerControllerBottomView.setLayoutParams(params);
                playerControllerBottomView
                        .setBackgroundColor(playerControllerBottomView
                                .getResources().getColor(
                                        R.color.player_color_88000000));
                playerControllerBottomView.setGravity(Gravity.CENTER_VERTICAL);
                playerControllerBottomView.setVisibility(View.GONE);

                this.mPlayerView.addView(playerControllerBottomView,
                        INDEX_CONTROLLER_BOTTOM_VIEW);
            }
        }
    }

    @Override
    public SurfaceView getSurfaceView() {
        return this.mSurfaceView;
    }

    @Override
    public boolean isPlayerControllerViewShowing() {
        if (this.mIPlayerControllerViewInterface != null
                && this.mIPlayerControllerViewInterface
                        .getPlayerControllerUpperView() != null) {
            return this.mIPlayerControllerViewInterface
                    .getPlayerControllerUpperView().getVisibility() == View.VISIBLE;
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
