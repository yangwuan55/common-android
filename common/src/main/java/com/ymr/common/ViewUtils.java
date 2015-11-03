package com.ymr.common;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ymr on 15/10/15.
 */
public class ViewUtils {
    public static void setEnabledAll(View v, boolean enabled) {
        v.setEnabled(enabled);
        /*v.setFocusable(enabled);
        v.setClickable(enabled);*/

        if(v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++)
                setEnabledAll(vg.getChildAt(i), enabled);
        }
    }
}
