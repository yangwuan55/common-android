package com.ymr.common;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by ymr on 15/10/19.
 */
public class UmengStatModel implements IStatModel {
    @Override
    public void onEvent(Context context, String action) {
        MobclickAgent.onEvent(context, action);
    }
}
