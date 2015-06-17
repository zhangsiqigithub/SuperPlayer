package com.dragon.superplayer.player.callback;

/**
 * 控制面板点击回调接口
 * @author yeguolong
 */
public interface PlayControllerViewCallback {
    /**
     * 当播放暂停按钮点击
     */
    public void onPlayPauseButtonClick();

    /**
     * 当横竖屏切换按钮点击
     */
    public void onOrientationSwitchButtonClick();
}