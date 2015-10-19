package com.ymr.common.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.ymr.common.Env;
import com.ymr.common.IStatModel;

/**
 * Created by ymr on 15/7/13.
 */
public class StatisticalHelper {

    public static void doStatistical(Context context, String id) {
        for (IStatModel statModel : Env.sStatModels) {
            statModel.onEvent(context,id);
        }
    }

}
