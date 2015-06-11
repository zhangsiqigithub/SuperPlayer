package com.dragon.superplayer.controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.dragon.superplayer.callback.PlayControllerCallback;
import com.dragon.superplayer.callback.PlayControllerViewCallback;
import com.dragon.superplayer.gesture.GestureCallback;
import com.dragon.superplayer.interfaces.IMediaPlayer;
import com.dragon.superplayer.interfaces.IViewController;
import com.dragon.superplayer.mediaplayer.BaseMediaPlayer;
import com.dragon.superplayer.mediaplayer.MediaPlayerListener;
import com.dragon.superplayer.mediaplayer.PlayStatus;
import com.dragon.superplayer.util.LogUtil;
import com.dragon.superplayer.view.controllerview.DefaultPlayControllerView.PlayPauseBtnStatus;

/**
 * 视频播放控制实现类：IMediaPlayer和IViewController的持有者。
 * @author yeguolong
 */
public class VideoPlayController extends BasePlayController {

    private IViewController mIViewController;
    private PlayControllerCallback mPlayControllerCallback;

    private static final int DELAY_TIME_TO_HIDE_PLAYER_CONTROLLER_VIEW = 3000;// 隐藏控制面板
    private static final int PLAYER_TIMER_PERIOD = 1000;// 定时器周期

    private boolean isSurfaceCreate = false;

    private static final int SHOW_PLAYER_CONTROLLER_VIEW = 1001;// 显示控制面板
    private static final int HIDE_PLAYER_CONTROLLER_VIEW = 1002;// 隐藏控制面板
    private static final int PLAYER_TIMER = 1003;// 获取播放进度的定时器
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case SHOW_PLAYER_CONTROLLER_VIEW:// 显示控制面板
                if (VideoPlayController.this.mIViewController != null) {
                    VideoPlayController.this.mIViewController
                            .showPlayerControllerView();
                }
                break;
            case HIDE_PLAYER_CONTROLLER_VIEW:// 隐藏控制面板
                if (VideoPlayController.this.mIViewController != null) {
                    VideoPlayController.this.mIViewController
                            .hidePlayerControllerView();
                }
                break;
            case PLAYER_TIMER:// 获取播放进度的定时器
                VideoPlayController.this.updateCurrentPlayState();
                this.sendEmptyMessageDelayed(PLAYER_TIMER, PLAYER_TIMER_PERIOD);
                break;

            default:
                break;
            }
        };
    };

    public VideoPlayController(Context context) {
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
        this.mIViewController.getSurfaceHolder().addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                VideoPlayController.this.log("surfaceDestroyed");
                VideoPlayController.this.isSurfaceCreate = false;
                if (VideoPlayController.this.mIMediaPlayer != null) {
                    if (VideoPlayController.this.mIMediaPlayer.getPlayStatus() == PlayStatus.PLAYING) {
                        VideoPlayController.this.doPause();
                    }
                    VideoPlayController.this.mIMediaPlayer.setDisPlay(null);
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                VideoPlayController.this.log("surfaceCreated");
                VideoPlayController.this.isSurfaceCreate = true;
                if (VideoPlayController.this.mIMediaPlayer != null) {
                    if (VideoPlayController.this.mIMediaPlayer.getPlayStatus() == PlayStatus.PAUSED) {
                        VideoPlayController.this.doPlay();
                        VideoPlayController.this.startPlayerTimer();
                    }
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
        this.mIViewController.setGestureCallBack(new GestureCallback() {
            @Override
            public void onSingleTapUp() {
                super.onSingleTapUp();
                if (VideoPlayController.this.mIViewController != null) {
                    if (VideoPlayController.this.mIViewController
                            .isPlayerControllerViewShowing()) {
                        VideoPlayController.this.mIViewController
                                .hidePlayerControllerView();
                    } else {
                        VideoPlayController.this.mIViewController
                                .showPlayerControllerView();
                    }
                }
            }
        });
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
        this.mIViewController
                .setPlayerSeekBarChangeListener(new OnSeekBarChangeListener() {

                    int mProgress = 0;

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        VideoPlayController.this.doSeek(seekBar.getProgress());
                        VideoPlayController.this.doPlay();
                        VideoPlayController.this.startPlayerTimer();
                        this.mProgress = 0;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        this.mProgress = seekBar.getProgress();
                        // VideoPlayController.this.doPause();
                        VideoPlayController.this.cancelPlayerTimer();
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
                    VideoPlayController.this.mIMediaPlayer
                            .setDisPlay(VideoPlayController.this.mIViewController
                                    .getSurfaceHolder());
                    VideoPlayController.this.mIViewController
                            .setPlayerSeekbarMax(VideoPlayController.this
                                    .getDuration());
                }
            }

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                VideoPlayController.this.log("onInfo-->what:" + what
                        + " extra:" + extra);
                switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:// 701

                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:// 702

                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:// 3

                    break;

                default:
                    break;
                }
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
            }

            @Override
            public void onBufferingEnd(MediaPlayer mp, int extra) {
                VideoPlayController.this.log("onBufferingEnd-->extra:" + extra);
            }

            @Override
            public void onVideoRenderingStart(MediaPlayer mp, int extra) {
                VideoPlayController.this.log("onVideoRenderingStart-->extra:"
                        + extra);
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
        this.cancelPlayerTimer();
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
            this.mIMediaPlayer.doPause();
        }
        if (this.mIViewController != null) {
            this.mIViewController.updatePlayPauseBtn(PlayPauseBtnStatus.PLAY);
        }
    }

    @Override
    public void doStop() {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.doStop();
        }
        this.cancelPlayerTimer();
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

    private void startPlayerTimer() {
        this.mHandler.sendEmptyMessage(PLAYER_TIMER);
    }

    private void cancelPlayerTimer() {
        this.mHandler.removeMessages(PLAYER_TIMER);
    }

    /**
     * 更新播放状态：<br>
     * 1、进度条<br>
     * 2、播放按钮<br>
     */
    private void updateCurrentPlayState() {
        if (this.mIViewController != null) {
            this.mIViewController.setPlayerSeekbarProgress(this
                    .getCurrentPosition());
        }
        this.updatePlayPauseBtnStatue();
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
    private void showPlayerControllerView(boolean isHideDelay) {
        this.mHandler.removeMessages(HIDE_PLAYER_CONTROLLER_VIEW);
        this.mHandler.sendEmptyMessage(SHOW_PLAYER_CONTROLLER_VIEW);
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
        this.mHandler.sendEmptyMessageDelayed(HIDE_PLAYER_CONTROLLER_VIEW,
                delayTime);
    }
}
