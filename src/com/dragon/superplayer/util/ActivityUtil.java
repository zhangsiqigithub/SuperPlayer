package com.dragon.superplayer.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

public class ActivityUtil {

    /**
     * 是否是横屏
     * @return
     */
    public static boolean isOrientationLandspace(Context context) {
        if (context != null) {
            return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        }
        return false;
    }

    /**
     * 解除屏幕方向锁定
     */
    public static void releaseOrientaionLock(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    /**
     * 希望Activity在纵向屏幕上显示，但是可以根据方向传感器指示的方向来进行改变。
     */
    public static void requestOrientationLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    /**
     * 希望Activity在横向屏幕上显示，但是可以根据方向传感器指示的方向来进行改变。
     */
    public static void requestOrientationPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }

}
