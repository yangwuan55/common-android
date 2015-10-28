package com.ymr.common;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by ymr on 15/9/15.
 */
public abstract class BaseApplication extends Application {
    private static RefWatcher sRefWacher;

    public static RefWatcher getRefWacher() {
        return sRefWacher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (getDebug()) {
            sRefWacher = LeakCanary.install(this);
        } else {
            sRefWacher = RefWatcher.DISABLED;
        }
    }

    protected abstract boolean getDebug();
}
