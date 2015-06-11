package com.dragon.superplayer.util;

public class TimeUtil {

    /**
     * 秒 --> 00:00:00
     * @param time
     *            秒
     * @return
     */
    public static String getStringTime(long time) {
        time /= 1000;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time >= 3600) {
            hour = time / 3600;
            time = time % 3600;
        }
        if (time >= 60) {
            minute = time / 60;
            time = time % 60;
        }
        second = time;
        if (hour == 0) {
            return formatTime(minute) + ":" + formatTime(second);
        }
        return formatTime(hour) + ":" + formatTime(minute) + ":"
                + formatTime(second);
    }

    private static String formatTime(long time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return "" + time;
        }
    }

}
