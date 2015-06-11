package com.dragon.superplayer.model;

public class LocalVideoInfo extends PlayItem {

    /**
     * 视频缩略图地址
     */
    private String mThumbPath;

    /**
     * 视频缩略图宽度
     */
    private int mThumbWidth;

    /**
     * 视频缩略图高度
     */
    private int mThumbHight;

    public void setThumbPath(String thumbPath) {
        this.mThumbPath = thumbPath;
    }

    public String getThumbPath() {
        return this.mThumbPath;
    }

    public int getThumbWidth() {
        return mThumbWidth;
    }

    public void setThumbWidth(int thumbWidth) {
        this.mThumbWidth = thumbWidth;
    }

    public int getThumbHight() {
        return mThumbHight;
    }

    public void setThumbHight(int thumbHight) {
        this.mThumbHight = thumbHight;
    }

}
