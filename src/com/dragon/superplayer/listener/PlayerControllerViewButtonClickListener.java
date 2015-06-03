package com.dragon.superplayer.listener;

public interface PlayerControllerViewButtonClickListener {
    /**
     * 当播放暂停按钮点击
     */
    public void onPlayPauseButtonClick();

    /**
     * 当横竖屏切换按钮点击
     */
    public void onOrientationSwitchButtonClick();
}