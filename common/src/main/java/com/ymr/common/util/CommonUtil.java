package com.ymr.common.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by ymr on 15/8/19.
 */
public class CommonUtil {
    public static void exitApp(Context context) {
        context.sendBroadcast(new Intent(Constant.ACTION_EXIST));
    }
}
