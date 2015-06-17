package com.dragon.superplayer.player.callback;


/**
 * 播放控制回调接口
 * @author yeguolong
 */
public abstract class PlayControllerCallback {

    /**
     * 当横竖屏切换按钮点击
     */
    public void onOrientationSwitchButtonClick() {
    }

    /**
     * 获取屏幕方向状态
     * @return
     */
    public boolean isScreenOrientationLandspace() {
        return false;
    }

}
