package com.dragon.superplayer.player.controller.playcontroller;

import android.app.Activity;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.dragon.superplayer.player.controller.playcontroller.callback.PlayControllerCallback;
import com.dragon.superplayer.player.controller.viewcontroller.interfaces.IViewController;
import com.dragon.superplayer.player.mediaplayer.BaseMediaPlayer;
import com.dragon.superplayer.player.mediaplayer.MediaPlayerListener;
import com.dragon.superplayer.player.mediaplayer.interfaces.IMediaPlayer;
import com.dragon.superplayer.player.model.PlayItem;
import com.dragon.superplayer.player.util.LogUtil;

/**
 * 音乐播放控制实现类：IMediaPlayer的持有者。
 * @author yeguolong
 */
public class MusicPlayController extends BasePlayController {

    public MusicPlayController(Activity context) {
        super(context);
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
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.startPlay(playUrl);
        }
    }

    @Override
    public synchronized void startPlay(String playUrl, int startPosition) {
        if (TextUtils.isEmpty(playUrl)) {
            return;
        }
        this.mPlayItem = new PlayItem();
        this.mPlayItem.setPlayUrl(playUrl);
        this.mPlayItem.setStartPostion(startPosition);
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.startPlay(playUrl);
        }
    }

    @Override
    protected IMediaPlayer initMediaPlayer() {
        this.mIMediaPlayer = new BaseMediaPlayer(this.mContext);
        this.mIMediaPlayer.setMediaPlayerListener(new MediaPlayerListener() {

            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                MusicPlayController.this.log("onVideoSizeChanged-->width:"
                        + width + " height:" + height);
            }

            @Override
            public void onPrepared(MediaPlayer mp) {
                MusicPlayController.this.log("onPrepared");
                if (MusicPlayController.this.mIMediaPlayer != null) {
                    MusicPlayController.this.mIMediaPlayer.doPlay();
                }
            }

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                MusicPlayController.this.log("onInfo-->what:" + what
                        + " extra:" + extra);
                return false;
            }

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                MusicPlayController.this.log("onError-->what:" + what
                        + " extra:" + extra);
                return false;
            }

            @Override
            public void onCompletion(MediaPlayer mp) {
                MusicPlayController.this.log("onCompletion");
            }

            @Override
            public void onBufferingStart(MediaPlayer mp, int extra) {
                MusicPlayController.this.log("onBufferingStart-->extra:"
                        + extra);
            }

            @Override
            public void onBufferingEnd(MediaPlayer mp, int extra) {
                MusicPlayController.this.log("onBufferingEnd-->extra:" + extra);
            }

            @Override
            public void onVideoRenderingStart(MediaPlayer mp, int extra) {
                MusicPlayController.this.log("onVideoRenderingStart-->extra:"
                        + extra);
            }

            @Override
            public void onMediaInfoUpdate(int currentPosition) {
                // TODO Auto-generated method stub

            }
        });
        return this.mIMediaPlayer;
    }

    @Override
    public void release() {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.gcMediaPlayer();
        }
    }

    private void log(String log) {
        LogUtil.d("MusicPlayerManager-->" + log);
    }

    @Override
    public void setPlayControllerCallback(
            PlayControllerCallback playControllerCallback) {

    }

    @Override
    public void doPlay() {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.doPlay();
        }
    }

    @Override
    public void doPause() {
        if (this.mIMediaPlayer != null) {
            this.mIMediaPlayer.doPause();
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
    public IViewController getViewController() {
        return null;
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

}
