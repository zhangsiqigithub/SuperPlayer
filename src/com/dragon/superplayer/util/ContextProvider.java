package com.dragon.superplayer.util;

import android.content.Context;

/**
 * 应用上下文的提供者，持有应用的Context。防止有些情况下，使用的上下文被回收。所以上下文尽量使用ApplicationContext
 * @author yeguolong
 */
public class ContextProvider {

    private static ContextProvider instance = null;
    private Context mContext;

    private ContextProvider() {
    }

    private static ContextProvider getInstance() {
        if (instance == null) {
            instance = new ContextProvider();
        }
        return instance;
    }

    public static void setApplicationContext(Context context) {
        getInstance().mContext = context;
    }

    public static Context getApplicationContext() {
        return getInstance().mContext;
    }

}
