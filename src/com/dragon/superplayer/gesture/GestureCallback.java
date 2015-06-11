package com.dragon.superplayer.gesture;

import android.view.MotionEvent;

/**
 * 手势callback
 */
public abstract class GestureCallback {
    /**
     * 双指下划
     */
    public void onDown() {
    }

    /**
     * 双指下划
     */
    public void onDoubleFingersDown() {
    }

    /**
     * 双指上划
     */
    public void onDoubleFingersUp() {
    }

    /**
     * 单击
     */
    public void onSingleTapUp() {
    }

    /**
     * 双击
     */
    public void onDoubleTap() {
    }

    /**
     * 右边上下滑动 变化总量
     */
    public void onRightScroll(float incremental) {
    }

    /**
     * 左边上下滑动 变化总量
     */
    public void onLeftScroll(float incremental) {
    }

    /**
     * 横向滑动 变化总量
     */
    public void onLandscapeScroll(float incremental) {
    }

    /**
     * 横向滑动 变化总量
     */
    public void onLandscapeScrollFinish(float incremental) {
    }

    /**
     * 中间单指上滑
     */
    public void onMiddleSingleFingerUp() {
    }

    /**
     * 中间单指下滑
     */
    public void onMiddleSingleFingerDown() {
    }

    /**
     * up事件，完成所有操作了
     */
    public void onTouchEventUp() {
    }

    /**
     * 长按
     */
    public void onLongPress() {
    }

    /**
     * 接触屏幕即出发，用于保持屏幕唤醒
     */
    public void onTouch(MotionEvent event) {
    }
}