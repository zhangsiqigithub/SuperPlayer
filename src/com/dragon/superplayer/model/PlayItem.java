package com.dragon.superplayer.model;

/**
 * 播放视频/音频介质的载体类
 * @author yeguolong
 */
public class PlayItem {

    /**
     * 播放地址
     */
    private String mPlayUrl;

    /**
     * 起播时间
     */
    private int mStartPostion;

    public void setPlayUrl(String playUrl) {
        this.mPlayUrl = playUrl;
    }

    public String getPlayUrl() {
        return this.mPlayUrl;
    }

    public void setStartPostion(int startPostion) {
        this.mStartPostion = startPostion;
    }

    public int getStartPostion() {
        return this.mStartPostion;
    }

}
