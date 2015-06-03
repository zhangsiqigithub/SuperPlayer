package com.dragon.superplayer.manager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.BatteryManager;
import android.view.ViewGroup.LayoutParams;

import com.dragon.superplayer.callback.PlayControllerCallback;
import com.dragon.superplayer.controller.VideoPlayController;
import com.dragon.superplayer.device.ScreenStatus;
import com.dragon.superplayer.interfaces.IPlayControler;
import com.dragon.superplayer.interfaces.IViewController;
import com.dragon.superplayer.util.ActivityUtil;
import com.dragon.superplayer.util.ContextProvider;
import com.dragon.superplayer.util.LogUtil;

/**
 * 播放管理者，（单路播放：非单例）<br>
 * 与内部和外部交互的入口类，持有：IPlayControler
 * @author yeguolong
 */
public class VideoPlayerManager {

    private static final float DEFAULT_PLAYER_HEIGHT_WIDTH_RATIO = 9.0f / 16.0f;// 默认播放器宽高比

    private static final String ACTION_VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";// 声音改变
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";// 网络改变

    private Activity mActivity;

    private IPlayControler mIPlayControler;
    private BroadcastReceiver mBroadcastReceiver;
    private final PlayerConfig mPlayerConfig;

    public VideoPlayerManager(Activity activity) {
        this.mPlayerConfig = new PlayerConfig();
        this.init(activity);
    }

    public VideoPlayerManager(Activity activity, PlayerConfig playerConfig) {
        this.mPlayerConfig = playerConfig;
        this.init(activity);
    }

    private void init(Activity activity) {
        this.log("init-->context:" + activity);
        if (activity == null) {
            return;
        }
        this.mActivity = activity;
        ContextProvider.setApplicationContext(this.mActivity
                .getApplicationContext());
        this.initVideoPlayController();
        this.registerBroadcastReceiver();
        ActivityUtil.releaseOrientaionLock(this.mActivity);
        this.updatePlayerLayout(ScreenStatus.SMALLSCREEN);
    }

    private void initVideoPlayController() {
        this.mIPlayControler = new VideoPlayController(this.mActivity);
        this.mIPlayControler
                .setPlayControllerCallback(new PlayControllerCallback() {
                    @Override
                    public void onOrientationSwitchButtonClick() {
                        super.onOrientationSwitchButtonClick();
                        VideoPlayerManager.this.toggleScreenOrientation();
                    }
                });
    }

    /**
     * 当屏幕方向发生改变时的处理
     * @param newConfig
     */
    public void onScreenOrientationChanged(Configuration newConfig) {
        switch (newConfig.orientation) {
        case Configuration.ORIENTATION_LANDSCAPE:// 横屏
            this.updatePlayerLayout(ScreenStatus.FULLSCREEN);
            break;
        case Configuration.ORIENTATION_PORTRAIT:// 竖屏
            this.updatePlayerLayout(ScreenStatus.SMALLSCREEN);
            break;
        }
    }

    /**
     * 切换屏幕方向
     */
    private void toggleScreenOrientation() {
        switch (this.getCurrentOrientation()) {
        case Configuration.ORIENTATION_LANDSCAPE:// 横屏
            this.changeOrientation(ScreenStatus.SMALLSCREEN);
            break;
        case Configuration.ORIENTATION_PORTRAIT:// 竖屏
            this.changeOrientation(ScreenStatus.FULLSCREEN);
            break;
        }
    }

    /**
     * 获取当前屏幕类型
     * @return
     */
    private ScreenStatus getCurrentScreenStatus() {
        ScreenStatus screenStatus = ScreenStatus.SMALLSCREEN;
        switch (this.getCurrentOrientation()) {
        case Configuration.ORIENTATION_LANDSCAPE:// 横屏
            screenStatus = ScreenStatus.FULLSCREEN;
            break;
        case Configuration.ORIENTATION_PORTRAIT:// 竖屏
            screenStatus = ScreenStatus.SMALLSCREEN;
            break;
        }
        return screenStatus;
    }

    /**
     * 获取当前横竖屏状态值
     * @return
     */
    private int getCurrentOrientation() {
        if (ContextProvider.getApplicationContext() != null) {
            return ContextProvider.getApplicationContext().getResources()
                    .getConfiguration().orientation;
        }
        return Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 主动改变屏幕方向
     * @param screenStatus
     */
    private void changeOrientation(ScreenStatus screenStatus) {
        this.log("changeOrientation-->screenStatus:" + screenStatus);
        switch (screenStatus) {
        case FULLSCREEN:// 全屏
            ActivityUtil.requestOrientationLandscape(this.mActivity);
            break;
        case SMALLSCREEN:// 半屏
            ActivityUtil.requestOrientationPortrait(this.mActivity);
            break;
        }
    }

    private void updatePlayerLayout(ScreenStatus screenStatus) {
        android.view.ViewGroup.LayoutParams layoutParams = this.mIPlayControler
                .getViewController().getSurfaceView().getLayoutParams();
        int screenWidth = this.mActivity.getResources().getDisplayMetrics().widthPixels;
        switch (screenStatus) {
        case FULLSCREEN:// 全屏
            layoutParams.width = LayoutParams.MATCH_PARENT;
            layoutParams.height = LayoutParams.MATCH_PARENT;
            break;
        case SMALLSCREEN:// 半屏
            layoutParams.width = screenWidth;
            layoutParams.height = (int) (screenWidth * DEFAULT_PLAYER_HEIGHT_WIDTH_RATIO);
            break;
        }
        this.mIPlayControler.getViewController().getPlayerView()
                .setLayoutParams(layoutParams);
    }

    /**
     * 注册广播监听：<br>
     * 所有广播均有这里监听，只允许有一个监听入口，持有监听的广播：<br>
     * 1、网络变化。<br>
     * 2、声音变化。<br>
     * 3、电量变化。<br>
     * 4、锁屏相关。<br>
     */
    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_VOLUME_CHANGED);// 声音改变
        intentFilter.addAction(ACTION_CONNECTIVITY_CHANGE);// 网络改变
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);// 开屏
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);// 锁屏
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);// 解锁
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);// 电量改变
        this.mBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (ACTION_VOLUME_CHANGED.equals(action)) {// 声音改变

                } else if (ACTION_CONNECTIVITY_CHANGE.equals(action)) {// 网络改变

                } else if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {// 电量改变

                    int voltage = intent.getIntExtra(
                            BatteryManager.EXTRA_VOLTAGE, -1);

                    // 取得系统当前电量
                    int level = intent.getIntExtra("level", 0);
                    // 取得系统总电量
                    int total = intent.getIntExtra("scale", 100);
                    // 当电量小于15%时触发
                    if (level < 15) {
                    }
                } else if (Intent.ACTION_SCREEN_ON.equals(action)) {// 开屏

                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {// 锁屏

                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {// 解锁

                }
            }
        };
        if (this.mActivity != null) {
            try {
                this.mActivity.registerReceiver(this.mBroadcastReceiver,
                        intentFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注销广播监听
     */
    private void unregisterBroadcastReceiver() {
        if (this.mActivity != null && this.mBroadcastReceiver != null) {
            try {
                this.mActivity.unregisterReceiver(this.mBroadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取播放控制接口
     * @return
     */
    public IPlayControler getPlayController() {
        return this.mIPlayControler;
    }

    /**
     * 获取播放控制接口
     * @return
     */
    public IViewController getViewController() {
        return this.mIPlayControler.getViewController();
    }

    /**
     * 销毁播放器
     */
    public void release() {
        this.log("release");
        this.unregisterBroadcastReceiver();
        if (this.mIPlayControler != null) {
            this.mIPlayControler.release();
        }
    }

    private void log(String log) {
        LogUtil.d("VideoPlayerManager-->" + log);
    }

    /**
     * 播放器相关配置类：可以配置一些播放相关的功能。<br>
     * 可以不配置，如果不配置，则使用默认的。<br>
     * 内置功能：<br>
     * 1、初始屏幕状态<br>
     * @author yeguolong
     */
    public static class PlayerConfig {

        private ScreenStatus mDefaultScreenStatus;;

        /**
         * 可以不配置，如果不配置，则使用默认的<br>
         * 初始化播放相关配置可以配置一些播放相关的功能：<br>
         * 1、初始屏幕状态<br>
         */
        public PlayerConfig() {

        }

        public void setDefaultScreenStatus(ScreenStatus screenStatus) {
            this.mDefaultScreenStatus = screenStatus;
        }

        public ScreenStatus getScreenStatus() {
            return this.mDefaultScreenStatus;
        }

    }

}
