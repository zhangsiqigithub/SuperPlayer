package com.dragon.superplayer.player.mediaplayer;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.view.SurfaceHolder;

import com.dragon.superplayer.player.mediaplayer.interfaces.IMediaPlayer;
import com.dragon.superplayer.player.util.LogUtil;

/**
 * MediaPlayer播放接口实现基类：<br>
 * 持有最基本的播放器（MediaPlayer）、播放状态（PlayStatus）。<br>
 * @author yeguolong
 */
public class BaseMediaPlayer implements IMediaPlayer {

    private static final int TIMER_PERIOD = 1;// 定时器周期

    private final Context mContext;
    private MediaPlayer mMediaPlayer;
    private MediaPlayerListener mMediaPlayerListener;
    private PlayStatus mPlayStatus = PlayStatus.STOPED;
    private int mCurrentPosition = 0;

    private final ScheduledExecutorService mScheduledExecutorService;
    private ScheduledFuture<?> mScheduledFuture;

    public BaseMediaPlayer(Context context) {
        this.mContext = context;
        this.mScheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void startPlay(String url) {
        this.log("startPlay-->url:" + url);
        if (this.mContext == null || url == null || url.equals("")) {
            return;
        }
        if (this.mMediaPlayer == null) {
            this.initMediaPlayer();
        }
        this.doPrepare(url);
    }

    /**
     * 开始请求播放数据
     * @param url
     */
    protected synchronized void doPrepare(final String url) {
        this.log("doPrepare");
        if (this.mMediaPlayer != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    FileInputStream is = null;
                    try {
                        if (url.startsWith("http")) {
                            BaseMediaPlayer.this.mMediaPlayer.setDataSource(
                                    BaseMediaPlayer.this.mContext,
                                    Uri.parse(url));
                        } else {
                            File file = new File(url);
                            if (file.exists()) {
                                is = new FileInputStream(file);
                                FileDescriptor fd = is.getFD();
                                BaseMediaPlayer.this.mMediaPlayer
                                        .setDataSource(fd);
                            }
                        }
                        BaseMediaPlayer.this.mMediaPlayer.prepareAsync();
                    } catch (Exception e) {
                        e.printStackTrace();
                        BaseMediaPlayer.this.log("setDataSource-->error:"
                                + e.getMessage());
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }.start();
        }
    }

    protected synchronized void initMediaPlayer() {
        this.log("initMediaPlayer");
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mMediaPlayer.setOnInfoListener(new OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:// 缓冲开始
                    BaseMediaPlayer.this.mPlayStatus = PlayStatus.BUFFERRING;
                    if (BaseMediaPlayer.this.mMediaPlayerListener != null) {
                        BaseMediaPlayer.this.mMediaPlayerListener
                                .onBufferingStart(mp, extra);
                    }
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:// 缓冲结束
                    BaseMediaPlayer.this.mPlayStatus = PlayStatus.PLAYING;
                    if (BaseMediaPlayer.this.mMediaPlayerListener != null) {
                        BaseMediaPlayer.this.mMediaPlayerListener
                                .onBufferingEnd(mp, extra);
                    }
                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:// 起播
                    BaseMediaPlayer.this.mPlayStatus = PlayStatus.PLAYING;
                    if (BaseMediaPlayer.this.mMediaPlayerListener != null) {
                        BaseMediaPlayer.this.mMediaPlayerListener
                                .onVideoRenderingStart(mp, extra);
                    }
                    break;
                }
                if (BaseMediaPlayer.this.mMediaPlayerListener != null) {
                    return BaseMediaPlayer.this.mMediaPlayerListener.onInfo(mp,
                            what, extra);
                }
                return false;
            }
        });
        this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                BaseMediaPlayer.this.mPlayStatus = PlayStatus.COMPLETED;
                if (BaseMediaPlayer.this.mMediaPlayerListener != null) {
                    BaseMediaPlayer.this.mMediaPlayerListener.onCompletion(mp);
                }
            }
        });
        this.mMediaPlayer.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (BaseMediaPlayer.this.mMediaPlayerListener != null) {
                    return BaseMediaPlayer.this.mMediaPlayerListener.onError(
                            mp, what, extra);
                }
                return false;
            }
        });
        this.mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                BaseMediaPlayer.this.mPlayStatus = PlayStatus.PREPARED;
                if (BaseMediaPlayer.this.mMediaPlayerListener != null) {
                    BaseMediaPlayer.this.mMediaPlayerListener.onPrepared(mp);
                }
            }
        });
        this.mMediaPlayer
                .setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {

                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width,
                            int height) {
                        if (BaseMediaPlayer.this.mMediaPlayerListener != null) {
                            BaseMediaPlayer.this.mMediaPlayerListener
                                    .onVideoSizeChanged(mp, width, height);
                        }
                    }
                });
    }

    @Override
    public PlayStatus getPlayStatus() {
        return this.mPlayStatus;
    }

    @Override
    public void setDisPlay(SurfaceHolder surfaceHolder) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setDisplay(surfaceHolder);
        }
    }

    @Override
    public void doPlay() {
        this.log("doPlay");
        if (this.mPlayStatus != PlayStatus.PLAYING && this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.start();
                this.mPlayStatus = PlayStatus.PLAYING;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            this.startTimer();
        }
    }

    @Override
    public synchronized void doSeek(int targetMsecPosition) {
        this.log("doSeek-->targetMsecPosition:" + targetMsecPosition);
        if (this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.seekTo(targetMsecPosition);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean doPause() {
        this.log("doPause");
        if (this.mPlayStatus == PlayStatus.PLAYING && this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.pause();
                this.mPlayStatus = PlayStatus.PAUSED;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            this.stopTimer();
            return true;
        }
        return false;
    }

    @Override
    public void doStop() {
        this.log("doStop");
        if (this.mPlayStatus != PlayStatus.STOPED && this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.stop();
                this.mPlayStatus = PlayStatus.STOPED;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            this.stopTimer();
        }
    }

    @Override
    public synchronized void gcMediaPlayer() {
        this.log("gcMediaPlayer");
        this.stopTimer();
        if (this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            try {
                this.mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mMediaPlayer = null;
        }
    }

    @Override
    public void setMediaPlayerListener(MediaPlayerListener mediaPlayerListener) {
        LogUtil.d("setMediaPlayerListener");
        this.mMediaPlayerListener = mediaPlayerListener;
    }

    private void log(String log) {
        LogUtil.d("BaseMediaPlayer-->" + log);
    }

    @Override
    public int getCurrentPosition() {
        return this.mCurrentPosition;
    }

    @Override
    public int getDuration() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public void startTimer() {
        if (this.mScheduledExecutorService != null
                && this.mScheduledFuture == null) {// mScheduledFuture如果不为空，说明正在执行
            this.mScheduledFuture = this.mScheduledExecutorService
                    .scheduleAtFixedRate(new Runnable() {

                        @Override
                        public void run() {
                            if (BaseMediaPlayer.this.mMediaPlayer != null) {
                                BaseMediaPlayer.this.mCurrentPosition = BaseMediaPlayer.this.mMediaPlayer
                                        .getCurrentPosition();
                                BaseMediaPlayer.this.mMediaPlayerListener
                                        .onMediaInfoUpdate(BaseMediaPlayer.this.mCurrentPosition);
                            }
                        }
                    }, 0, TIMER_PERIOD, TimeUnit.SECONDS);
        }
    }

    @Override
    public void stopTimer() {
        if (this.mScheduledFuture != null) {
            this.mScheduledFuture.cancel(true);
            this.mScheduledFuture = null;
        }
    }
}
