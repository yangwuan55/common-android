package com.ymr.common.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by ymr on 15/7/13.
 */
public class StatisticalHelper {

    public static void doStatistical(Context context, String id) {
        umeng(context, id);
    }

    public static void umeng(Context context, String id) {
        MobclickAgent.onEvent(context, id);
    }

}
