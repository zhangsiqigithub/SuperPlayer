package com.dragon.superplayer.player.controller.playcontroller;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.dragon.superplayer.player.controller.playcontroller.callback.PlayControllerCallback;
import com.dragon.superplayer.player.controller.viewcontroller.ViewController;
import com.dragon.superplayer.player.controller.viewcontroller.interfaces.IViewController;
import com.dragon.superplayer.player.gesture.GestureCallback;
import com.dragon.superplayer.player.mediaplayer.BaseMediaPlayer;
import com.dragon.superplayer.player.mediaplayer.MediaPlayerListener;
import com.dragon.superplayer.player.mediaplayer.PlayStatus;
import com.dragon.superplayer.player.mediaplayer.interfaces.IMediaPlayer;
import com.dragon.superplayer.player.model.PlayItem;
import com.dragon.superplayer.player.util.LogUtil;
import com.dragon.superplayer.player.view.DefaultPlayControllerView.PlayPauseBtnStatus;
import com.dragon.superplayer.player.view.callback.PlayControllerViewCallback;

/**
 * 视频播放控制实现类：IMediaPlayer和IViewController的持有者。
 * @author yeguolong
 */
public class VideoPlayController extends BasePlayController {

    private IViewController mIViewController;
    private PlayControllerCallback mPlayControllerCallback;

    private static final int DELAY_TIME_TO_HIDE_PLAYER_CONTROLLER_VIEW = 3000;// 隐藏控制面板

    // 竖屏时从左滑到右最多快进2分钟
    protected static final int MAX_FORWARD_PROGRESS_PORTRAIT = 2 * 60 * 1000;
    // 横屏时从左滑到右最多快进3分钟
    protected static final int MAX_FORWARD_PROGRESS_LANDSCAPE = 3 * 60 * 1000;

    private boolean isSurfaceCreate = false;

    private static final int SHOW_PLAYER_CONTROLLER_VIEW = 1001;// 显示控制面板
    private static final int HIDE_PLAYER_CONTROLLER_VIEW = 1002;// 隐藏控制面板
    private static final int UPDATA_PLAY_STATE = 1003;// 获取播放进度的定时器
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case SHOW_PLAYER_CONTROLLER_VIEW:// 显示控制面板
                if (VideoPlayController.this.mIViewController != null) {
                    VideoPlayController.this.mIViewController
                            .showPlayerControllerView((boolean) msg.obj);
                }
                break;
            case HIDE_PLAYER_CONTROLLER_VIEW:// 隐藏控制面板
                if (VideoPlayController.this.mIViewController != null) {
                    VideoPlayController.this.mIViewController
                            .hidePlayerControllerView();
                }
                break;
            case UPDATA_PLAY_STATE:// 获取播放进度的定时器
                VideoPlayController.this.updateCurrentPlayState();
                break;

            default:
                break;
            }
        };
    };

    public VideoPlayController(Activity context) {
        super(context);

        this.init();
    }

    private void init() {
        this.initViewController();
        this.initMediaPlayer();
    }

    private void initViewController() {
        this.log("initViewController");
        this.mIViewController = new ViewController(this.mContext);
        // 初始化SurfaceView生命周期回调
        this.mIViewController.getSurfaceHolder().addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                VideoPlayController.this.log("surfaceDestroyed");
                VideoPlayController.this.isSurfaceCreate = false;
                if (VideoPlayController.this.mIMediaPlayer != null) {
                    VideoPlayController.this.mIMediaPlayer.setDisPlay(null);
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                VideoPlayController.this.log("surfaceCreated");
                VideoPlayController.this.isSurfaceCreate = true;
                if (VideoPlayController.this.mIMediaPlayer != null) {
                    VideoPlayController.this.mIMediaPlayer.setDisPlay(holder);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                    int width, int height) {
                VideoPlayController.this.log("surfaceChanged-->width:" + width
                        + " height:" + height);
                if (VideoPlayController.this.mIMediaPlayer != null) {
                    VideoPlayController.this.mIMediaPlayer.setDisPlay(holder);
                }
            }
        });
        // 初始化手势回调监听
        this.mIViewController.setGestureCallBack(new GestureCallback() {

            private float mLastGestureIncremental = 0.0f;

            @Override
            public void onSingleTapUp() {
                super.onSingleTapUp();
                if (VideoPlayController.this.mIViewController != null) {
                    if (VideoPlayController.this.mIViewController
                            .isPlayerControllerViewShowing()) {
                        VideoPlayController.this.hidePlayerControllerView(0);
                    } else {
                        VideoPlayController.this.showPlayerControllerView(true,
                                true);
                    }
                }
            }

            @Override
            public void onLandscapeScroll(float incremental) {
                super.onLandscapeScroll(incremental);
                this.dealSeekFromGesture(incremental, false);
            }

            @Override
            public void onLandscapeScrollFinish(float incremental) {
                super.onLandscapeScrollFinish(incremental);
                this.dealSeekFromGesture(incremental, true);
            }

            /**
             * 处理手势滑动seek
             * @param incremental
             * @param isEnd
             */
            private void dealSeekFromGesture(float incremental, boolean isEnd) {
                int duration = VideoPlayController.this.getDuration();
                if (duration <= 0) {
                    return;
                }
                // 当片源长度大于3min时，从屏幕左端滑动到右端进度条前进3min，否则等于整个视频的长度
                int maxProgress = 0;
                if (VideoPlayController.this.mPlayControllerCallback
                        .isScreenOrientationLandspace()) {
                    maxProgress = duration > MAX_FORWARD_PROGRESS_LANDSCAPE ? MAX_FORWARD_PROGRESS_LANDSCAPE
                            : duration;
                } else {
                    maxProgress = duration > MAX_FORWARD_PROGRESS_PORTRAIT ? MAX_FORWARD_PROGRESS_PORTRAIT
                            : duration;
                }

                int currentPosition = VideoPlayController.this
                        .getCurrentPosition();

                int targetPosition = currentPosition
                        + (int) (incremental * maxProgress);
                if (targetPosition < 0) {
                    targetPosition = 1;
                }

                if (targetPosition > duration) {
                    targetPosition = duration;
                }
                VideoPlayController.this.showPlayerControllerView(isEnd, true);
                if (isEnd) {
                    VideoPlayController.this.doSeek(targetPosition);
                    VideoPlayController.this.doPlay();
                    if (VideoPlayController.this.mIViewController != null) {
                        VideoPlayController.this.mIViewController
                                .updatePlayPauseBtn(PlayPauseBtnStatus.PAUSE);
                    }
                } else {
                    VideoPlayController.this.doPause();
                    if (VideoPlayController.this.mIViewController != null) {
                        VideoPlayController.this.mIViewController
                                .setPlayerSeekbarProgress(targetPosition);
                    }
                    boolean isFoward = incremental > this.mLastGestureIncremental;
                    if (isFoward) {// 快进
                        if (VideoPlayController.this.mIViewController != null) {
                            VideoPlayController.this.mIViewController
                                    .updatePlayPauseBtn(PlayPauseBtnStatus.FAST_RUN);
                        }
                    } else {// 快退
                        VideoPlayController.this.mIViewController
                                .updatePlayPauseBtn(PlayPauseBtnStatus.FAST_BACK);
                    }
                }
                if (isEnd) {
                    this.mLastGestureIncremental = 0.0f;
                } else {
                    this.mLastGestureIncremental = incremental;
                }
            }
        });
        // 初始化控制面板回调监听
        this.mIViewController
                .setPlayerControllerViewCallbackListener(new PlayControllerViewCallback() {

                    @Override
                    public void onPlayPauseButtonClick() {
                        if (VideoPlayController.this.getPlayStatus() == PlayStatus.PLAYING) {
                            VideoPlayController.this.doPause();
                        } else if (VideoPlayController.this.getPlayStatus() == PlayStatus.PAUSED) {
                            VideoPlayController.this.doPlay();
                        }
                    }

                    @Override
                    public void onOrientationSwitchButtonClick() {
                        if (VideoPlayController.this.mPlayControllerCallback != null) {
                            VideoPlayController.this.mPlayControllerCallback
                                    .onOrientationSwitchButtonClick();
                        }
                    }
                });
        // 初始化播放进度条监听
        this.mIViewController
                .setPlayerSeekBarChangeListener(new OnSeekBarChangeListener() {

                    int mProgress = 0;

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        VideoPlayController.this.doSeek(seekBar.getProgress());
                        VideoPlayController.this.doPlay();
                        VideoPlayController.this.showPlayerControllerView(true,
                                false);
                        this.mProgress = 0;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        this.mProgress = seekBar.getProgress();
                        VideoPlayController.this.doPause();
                        VideoPlayController.this.showPlayerControllerView(
                                false, false);
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                            int progress, boolean fromUser) {
                        if (this.mProgress != 0) {
                            if (seekBar.getProgress() > this.mProgress) {// 快进
                                if (VideoPlayController.this.mIViewController != null) {
                                    VideoPlayController.this.mIViewController
                                            .updatePlayPauseBtn(PlayPauseBtnStatus.FAST_RUN);
                                }
                            } else if (seekBar.getProgress() < this.mProgress) {// 快退
                                VideoPlayController.this.mIViewController
                                        .updatePlayPauseBtn(PlayPauseBtnStatus.FAST_BACK);
                            } else {// 位置没变
                                VideoPlayController.this.mIViewController
                                        .updatePlayPauseBtn(PlayPauseBtnStatus.PAUSE);
                            }
                        }
                    }
                });
    }

    @Override
    public synchronized void startPlay(PlayItem playItem) {
        if (playItem == null) {
            return;
        }
        this.mPlayItem = playItem;
        String playUrl = this.mPlayItem.getPlayUrl();
        if (TextUtils.isEmpty(playUrl)) {
            return;
        }
        this.play();
    }

    @Override
    public synchronized void startPlay(String playUrl, int startPosition) {
        if (TextUtils.isEmpty(playUrl)) {
            return;
        }
        this.mPlayItem = new PlayItem();
        this.mPlayItem.setPlayUrl(playUrl);
        this.mPlayItem.setStartPostion(startPosition);
        this.play();
    }

    /**
     * 起播
     */
    private void play() {
        if (this.mIMediaPlayer != null) {
            this.showLoading();
            this.mIMediaPlayer.startPlay(this.mPlayItem.getPlayUrl());
        }
    }

    private void showLoading() {
        if (this.mIViewController != null) {
            this.mIViewController.showLoading(null);
        }
    }

    private void hideLoading() {
        if (this.mIViewController != null) {
            this.mIViewController.hideLoading();
        }
    }

    @Override
    protected IMediaPlayer initMediaPlayer() {
        this.log("initMediaPlayer");
        this.mIMediaPlayer = new BaseMediaPlayer(this.mContext);
        this.mIMediaPlayer.setMediaPlayerListener(new MediaPlayerListener() {

            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                VideoPlayController.this.log("onVideoSizeChanged-->width:"
                        + width + " height:" + height);
                // 根据视频原始宽高，重置SurfaceView的宽高
                int videoWidth = mp.getVideoWidth();
                int videoHeight = mp.getVideoHeight();
                VideoPlayController.this.mIViewController.getSurfaceHolder()
                        .setFixedSize(videoWidth, videoHeight);
            }

            @Override
            public void onPrepared(MediaPlayer mp) {
                VideoPlayController.this.log("onPrepared-->duration:"
                        + VideoPlayController.this.getDuration());
                if (VideoPlayController.this.mIMediaPlayer != null
                        && VideoPlayController.this.mIViewController != null) {
                    // 设置总时长
                    VideoPlayController.this.mIViewController
                            .setPlayerSeekbarMax(VideoPlayController.this
                                    .getDuration());
                    // 起播
                    VideoPlayController.this.doPlay();
                }
            }

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                VideoPlayController.this.log("onInfo-->what:" + what
                        + " extra:" + extra);
                return false;
            }

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                VideoPlayController.this.log("onError-->what:" + what
                        + " extra:" + extra);
                return false;
            }

            @Override
            public void onCompletion(MediaPlayer mp) {
                VideoPlayController.this.log("onCompletion");
                VideoPlayController.this.updateCurrentPlayState();
            }

            @Override
            public void onBufferingStart(MediaPlayer mp, int extra) {
                VideoPlayController.this.log("onBufferingStart-->extra:"
                        + extra);
                VideoPlayController.this.showLoading();
            }

            @Override
            public void onBufferingEnd(MediaPlayer mp, int extra) {
                VideoPlayController.this.log("onBufferingEnd-->extra:" + extra);
                VideoPlayController.this.hideLoading();
            }

            @Override
            public void onVideoRenderingStart(MediaPlayer mp, int extra) {
                VideoPlayController.this.log("onVideoRenderingStart-->extra:"
                        + extra);
            }

            @Override
            public void onMediaInfoUpdate(int currentPosition) {
                VideoPlayController.this
                        .log("onMediaInfoUpdate-->currentPosition:"
                                + currentPosition);
                VideoPlayController.this.mHandler
                        .sendEmptyMessage(UPDATA_PLAY_STATE);
            }
        });
        return this.mIMediaPlayer;
    }

    @Override
    public IViewController getViewController() {
        return this.mIViewController;
    }

    @Override
    public void release() {
        this.log("release");
        this.gcMediaPlayer();
    }

    private void log(String log) {
        LogUtil.d("PlayController-->" + log);
    }

    @Override
    public void doPlay() {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.doPlay();
        }
        if (this.mIViewController != null) {
            this.mIViewController.updatePlayPauseBtn(PlayPauseBtnStatus.PAUSE);
        }
    }

    @Override
    public void doPause() {
        if (this.mIMediaPlayer != null) {
            if (this.mIMediaPlayer.doPause()) {
                if (this.mIViewController != null) {
                    this.mIViewController
                            .updatePlayPauseBtn(PlayPauseBtnStatus.PLAY);
                }
            }
        }
    }

    @Override
    public void doStop() {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.doStop();
        }
    }

    @Override
    public void doSeek(int targetPosition) {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.doSeek(targetPosition);
        }
    }

    @Override
    public void setPlayControllerCallback(
            PlayControllerCallback playControllerCallback) {
        this.mPlayControllerCallback = playControllerCallback;
    }

    /**
     * 更新播放状态：<br>
     * 1、进度条<br>
     */
    private void updateCurrentPlayState() {
        if (this.mIViewController != null) {
            this.mIViewController.setPlayerSeekbarProgress(this
                    .getCurrentPosition());
        }
    }

    /**
     * 更新播放按钮的状态
     */
    private void updatePlayPauseBtnStatue() {
        if (this.mIMediaPlayer != null) {
            switch (this.mIMediaPlayer.getPlayStatus()) {
            case PLAYING:
                if (this.mIViewController != null) {
                    this.mIViewController
                            .updatePlayPauseBtn(PlayPauseBtnStatus.PAUSE);
                }
                break;
            case PAUSED:
                if (this.mIViewController != null) {
                    this.mIViewController
                            .updatePlayPauseBtn(PlayPauseBtnStatus.PLAY);
                }
                break;
            default:
                if (this.mIViewController != null) {
                    this.mIViewController
                            .updatePlayPauseBtn(PlayPauseBtnStatus.STOP);
                }
                break;
            }
        }
    }

    /**
     * 获取当前播放进度
     * @return
     */
    @Override
    public int getCurrentPosition() {
        if (this.mIMediaPlayer != null) {
            return this.mIMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 获取播放总时长
     * @return
     */
    @Override
    public int getDuration() {
        if (this.mIMediaPlayer != null) {
            return this.mIMediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 显示控制面板
     * @param isHideDelay
     *            是否需要延迟隐藏控制面板
     */
    private void showPlayerControllerView(boolean isHideDelay,
            boolean useAnimate) {
        this.mHandler.removeMessages(HIDE_PLAYER_CONTROLLER_VIEW);
        Message msg = Message.obtain();
        msg.obj = useAnimate;
        msg.what = SHOW_PLAYER_CONTROLLER_VIEW;
        this.mHandler.sendMessage(msg);
        if (isHideDelay) {
            this.hidePlayerControllerView(DELAY_TIME_TO_HIDE_PLAYER_CONTROLLER_VIEW);
        }
    }

    /**
     * 隐藏控制面板
     * @param delayTime
     *            延迟隐藏时间，如需立即隐藏，则传入0即可
     */
    private void hidePlayerControllerView(long delayTime) {
        this.mHandler.removeMessages(HIDE_PLAYER_CONTROLLER_VIEW);
        this.mHandler.sendEmptyMessageDelayed(HIDE_PLAYER_CONTROLLER_VIEW,
                delayTime);
    }
}
