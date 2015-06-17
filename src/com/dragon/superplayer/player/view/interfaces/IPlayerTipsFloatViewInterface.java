package com.dragon.superplayer.player.view.interfaces;

import android.view.ViewGroup;

/**
 * 提示界面接口：<br>
 * 1、缓冲<br>
 * 2、亮度<br>
 * 3、音量<br>
 * @author yeguolong
 */
public interface IPlayerTipsFloatViewInterface {

    /**
     * 获取提示浮层layout
     * @return
     */
    public ViewGroup getPlayerTipsFloatView();

    /**
     * 显示缓冲
     * @param loadingText
     */
    public void showLoading(String loadingText);

    /**
     * 隐藏缓冲
     */
    public void hideLoading();

    /**
     * 显示亮度
     * @param value
     */
    public void showBrightness(int value);

    /**
     * 隐藏亮度
     */
    public void hideBrightness();

    /**
     * 显示音量
     * @param value
     */
    public void showVolume(int value);

    /**
     * 隐藏音量
     */
    public void hideVolume();

}
