package com.dragon.superplayer.controller;

import android.content.Context;
import android.media.MediaPlayer;

import com.dragon.superplayer.callback.PlayControllerCallback;
import com.dragon.superplayer.interfaces.IMediaPlayer;
import com.dragon.superplayer.mediaplayer.BaseMediaPlayer;
import com.dragon.superplayer.mediaplayer.MediaPlayerListener;
import com.dragon.superplayer.util.LogUtil;

/**
 * 音乐播放控制实现类：IMediaPlayer的持有者。
 * @author yeguolong
 */
public class MusicPlayController extends BasePlayController {

    public MusicPlayController(Context context) {
        super(context);
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

}
