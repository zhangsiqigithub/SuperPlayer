package com.dragon.superplayer.gesture;

import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.dragon.superplayer.util.ContextProvider;

public class GestureManager implements OnGestureListener, OnTouchListener {

    /**
     * 无事件
     */
    public static final int NONE = 0x00;
    /**
     * 双指下划
     */
    public static final int DOUBLE_FINGERS_DOWN = 0x10;
    /**
     * 双指上划
     */
    public static final int DOUBLE_FINGERS_UP = 0x11;
    /**
     * 横划完成
     */
    public static final int LANDSCAPE_SCROLL_FINISH = 0x12;
    /**
     * 单指下划
     */
    public static final int SINGLE_FINGERS_DOWN = 0x13;

    /**
     * 单指上划
     */
    public static final int SINGLE_FINGERS_UP = 0x14;

    /**
     * 手势回调
     */
    private GestureCallback mGestureCallback;

    /**
     * 当前手势事件
     */
    public int event;

    /**
     * 手势探测器
     */
    private GestureDetector mGestureDetector = null;

    /**
     * Y轴变化值，在一次事件完成后，会被归0
     */
    private float offsetY = 0;

    /**
     * X轴变化值，在一次事件完成后，会被归0
     */
    private float offsetX = 0;

    /**
     * 竖划斜率限制
     */
    private final float portraitLimitSlope = 4; // 上下滑动手势的限制斜率

    /**
     * 横划斜率限制
     */
    private final float landscapeLimitSlope = 1f / 4f; // 上下滑动手势的限制斜率

    /**
     * 方向锁
     */
    private int directionalLock = 0; // 0 无方向，1纵向，2横向

    /**
     * 双指上划伐值
     */
    private final float doubleFingersUpCuttingValue = 0.3f;

    /**
     * 双指下划伐值
     */
    private final float doubleFingersDownCuttingValue = 0.3f;

    /**
     * 屏幕两边上下滑动区域伐值
     */
    private final float bothSidesCuttingValue = 0.5f;

    /**
     * 右半屏长度
     */
    private float rightProgerss = 0;

    /**
     * 左半屏长度
     */
    private float leftProgress = 0;

    /**
     * 横划进度记录
     */
    private float landscapeProgress = 0;

    /**
     * 屏幕中间上下滑动区域伐值
     */
    private final float middleCuttingValue = 0.5f;
    /**
     * 防止滑动屏幕快进为抬起手指时，再用双指触发超级电视
     */
    private boolean isLandscapeScroll;

    private final View mDetectedView;

    public GestureManager(View view) {
        this.mDetectedView = view;
        this.mDetectedView.setOnTouchListener(this);
        this.init();
    }

    /**
     * 手势类内部初始化
     */
    protected void init() {
        this.mGestureDetector = new GestureDetector(this);
        this.mGestureDetector
                .setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                    /**
                     * 单击
                     */
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (GestureManager.this.mGestureCallback != null) {
                            GestureManager.this.mGestureCallback
                                    .onSingleTapUp();
                        }
                        return true;
                    }

                    /**
                     * 双击
                     */
                    @Override
                    public boolean onDoubleTapEvent(MotionEvent e) {
                        if (GestureManager.this.mGestureCallback != null
                                && e.getAction() == MotionEvent.ACTION_UP) {
                            GestureManager.this.mGestureCallback
                                    .onDoubleTap();
                        }
                        return true;
                    }

                    /**
                     * 双击
                     */
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        return true;
                    }
                });
    }

    /**
     * 初始化页面空间，请再控件初始化后在调用
     */
    public void initializeData(float rightProgerss, float leftProgress) {
        this.rightProgerss = rightProgerss;
        this.leftProgress = leftProgress;
    }

    private static boolean isLandscape() {
        return ContextProvider.getApplicationContext().getResources()
                .getConfiguration().orientation == 2;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        this.mGestureCallback.onTouch(event);
        if (MotionEvent.ACTION_UP == event.getAction()) {
            // 释放方向锁
            switch (this.event) {
            case DOUBLE_FINGERS_UP:
                if (this.mGestureCallback != null) {
                    if (isLandscape() && !this.isLandscapeScroll) {
                        this.mGestureCallback.onDoubleFingersUp();
                    }
                }
                break;
            case DOUBLE_FINGERS_DOWN:
                if (this.mGestureCallback != null) {
                    if (isLandscape() && !this.isLandscapeScroll) {
                        this.mGestureCallback.onDoubleFingersDown();
                    }
                }
                break;
            case LANDSCAPE_SCROLL_FINISH:
                if (this.mGestureCallback != null) {
                    this.mGestureCallback
                            .onLandscapeScrollFinish(this.landscapeProgress);
                }
                break;
            case SINGLE_FINGERS_UP:
                if (this.mGestureCallback != null) {
                    this.mGestureCallback.onMiddleSingleFingerUp();
                }
                break;
            case SINGLE_FINGERS_DOWN:
                if (this.mGestureCallback != null) {
                    this.mGestureCallback.onMiddleSingleFingerDown();
                }
            default:
                break;
            }

            this.directionalLock = 0;
            this.offsetY = 0;
            this.offsetX = 0;
            this.landscapeProgress = 0;
            this.event = NONE;

            if (this.mGestureCallback != null) {
                this.mGestureCallback.onTouchEventUp();
            }
            this.isLandscapeScroll = false;
        }
        return this.mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        this.leftProgress = 0;
        this.rightProgerss = 0;
        if (this.mGestureCallback != null) {
            this.mGestureCallback.onDown();
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /**
     * 完成手势走的调节声音和调节亮度的功能
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
        int s = e2.getPointerCount();
        if (s == 2) {// 双指滑动
            this.offsetY += distanceY;
            if (this.offsetY > 0) {
                if (this.offsetY > this.doubleFingersUpCuttingValue
                        * this.getHeight()) {// 向下
                    this.event = DOUBLE_FINGERS_UP;
                } else {// 距离不够，或者划回来就取消事件
                    this.event = NONE;
                }
            } else {
                if (this.offsetY < -this.doubleFingersDownCuttingValue
                        * this.getHeight()) {// 向上
                    this.event = DOUBLE_FINGERS_DOWN;
                } else {// 距离不够，或者划回来就取消事件
                    this.event = NONE;
                }
            }
        } else if (s == 1) {// 单指滑动
            if (Math.abs(distanceY) > this.portraitLimitSlope
                    * Math.abs(distanceX)
                    && this.directionalLock != 2) {// 斜率判断，竖划
                this.directionalLock = 1;
                if (e1.getX() > (1 - this.bothSidesCuttingValue)
                        * this.getWidth()) {// 右半屏幕上下滑动
                    this.rightProgerss += distanceY / this.getHeight();
                    Log.e("nicholas", "distanceY = " + distanceY
                            + ", height = " + this.getHeight()
                            + ", right progress = " + this.rightProgerss);
                    if (this.mGestureCallback != null) {
                        this.mGestureCallback.onRightScroll(this.rightProgerss);
                    }
                } else if (e1.getX() < this.bothSidesCuttingValue
                        * this.getWidth()) {// 左半屏幕上下滑动
                    this.leftProgress += distanceY / this.getHeight();
                    if (this.mGestureCallback != null) {
                        this.mGestureCallback.onLeftScroll(this.leftProgress);
                    }
                } else if (e1.getX() > (this.middleCuttingValue - this.bothSidesCuttingValue)
                        * this.getWidth()
                        && e1.getX() < (this.middleCuttingValue + this.bothSidesCuttingValue)
                                * this.getWidth()) {
                    this.offsetY += distanceY;
                    if (this.mGestureCallback != null) {
                        if (this.offsetY > this.bothSidesCuttingValue
                                * this.getHeight()) {// 单指向上滑动
                            this.event = SINGLE_FINGERS_UP;
                        } else if (this.offsetY < -this.bothSidesCuttingValue
                                * this.getHeight()) {// 单指向下滑动
                            this.event = SINGLE_FINGERS_DOWN;
                        }
                    }
                }
            } else if (Math.abs(distanceY) < this.landscapeLimitSlope
                    * Math.abs(distanceX)
                    && this.directionalLock != 1) {// 斜率判断，横划

                this.directionalLock = 2;
                this.offsetX -= distanceX;

                this.landscapeProgress = this.offsetX / this.getWidth();

                this.mGestureCallback.onLandscapeScroll(this.landscapeProgress);
                this.event = LANDSCAPE_SCROLL_FINISH;
                this.isLandscapeScroll = true;
            }
        }

        return false;
    }

    private int getHeight() {
        if (this.mDetectedView != null) {
            return this.mDetectedView.getHeight();
        }
        return 0;
    }

    private int getWidth() {
        if (this.mDetectedView != null) {
            return this.mDetectedView.getWidth();
        }
        return 0;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (this.mGestureCallback != null) {
            this.mGestureCallback.onLongPress();
        }
    }

    public GestureCallback getGestureCallback() {
        return this.mGestureCallback;
    }

    public void setGestureCallBack(GestureCallback LetvGestureCallBack) {
        this.mGestureCallback = LetvGestureCallBack;
    }
}
