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

    interface BaseUIParams {
        int getTitleBgColor();
        int getTitleTextColor();
        int getBackDrawable();
    }
    void onDestroy();
}
