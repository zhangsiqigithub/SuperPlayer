package com.dragon.superplayer.controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

import com.dragon.superplayer.callback.PlayControllerCallback;
import com.dragon.superplayer.interfaces.IMediaPlayer;
import com.dragon.superplayer.interfaces.IViewController;
import com.dragon.superplayer.listener.PlayerControllerViewButtonClickListener;
import com.dragon.superplayer.mediaplayer.BaseMediaPlayer;
import com.dragon.superplayer.mediaplayer.MediaPlayerListener;
import com.dragon.superplayer.mediaplayer.PlayStatus;
import com.dragon.superplayer.util.LogUtil;

/**
 * 视频播放控制实现类：IMediaPlayer和IViewController的持有者。
 * @author yeguolong
 */
public class VideoPlayController extends BasePlayController {

    private IViewController mIViewController;
    private PlayControllerCallback mPlayControllerCallback;

    public VideoPlayController(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        this.initViewController();
        this.initMediaPlayer();
        this.initPlayerControllerViewButtonClickListener();
    }

    private void initViewController() {
        this.log("initViewController");
        this.mIViewController = new ViewController(this.mContext);
        this.mIViewController.getSurfaceHolder().addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                VideoPlayController.this.log("surfaceDestroyed");
                if (VideoPlayController.this.mIMediaPlayer != null) {
                    VideoPlayController.this.mIMediaPlayer.setDisPlay(null);
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                VideoPlayController.this.log("surfaceCreated");
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
                int videoWidth = mp.getVideoWidth();
                int videoHeight = mp.getVideoHeight();
                VideoPlayController.this.mIViewController.getSurfaceHolder()
                        .setFixedSize(videoWidth, videoHeight);
            }

            @Override
            public void onPrepared(MediaPlayer mp) {
                VideoPlayController.this.log("onPrepared");
                // PlayController.this.resetSurfaceView();
                VideoPlayController.this.mIMediaPlayer
                        .setDisPlay(VideoPlayController.this.mIViewController
                                .getSurfaceHolder());
                VideoPlayController.this.mIMediaPlayer.doPlay();
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

    private void initPlayerControllerViewButtonClickListener() {
        if (this.mIViewController != null) {
            this.mIViewController
                    .setPlayerControllerViewButtonClickListener(new PlayerControllerViewButtonClickListener() {

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
        }
    }

    @Override
    public IViewController getViewController() {
        return this.mIViewController;
    }

    @Override
    public void release() {
        super.release();
        this.log("release");
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.gcMediaPlayer();
        }
    }

    private void log(String log) {
        LogUtil.d("PlayController-->" + log);
    }

    @Override
    public void setPlayControllerCallback(
            PlayControllerCallback playControllerCallback) {
        this.mPlayControllerCallback = playControllerCallback;
    }
}
