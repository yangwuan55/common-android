package com.ymr.common.ui;

import android.app.Activity;
import android.view.View;

/**
 * Created by ymr on 15/10/12.
 */
public interface IBaseUIController {
    BaseUIParams getBaseUIParams();

    void initActivity();

    void setTitle(String title);

    Activity getActivity();

    View getRootView();

    View getRightView();

    interface BaseUIParams {
        int getTitleBgColor();
        int getTitleTextColor();
        int getBackDrawable();
        int getTitleHeight();
        int getTitleDiverColor();
    }
    void onDestroy();
}
