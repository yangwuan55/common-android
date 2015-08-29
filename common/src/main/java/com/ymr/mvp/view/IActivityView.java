package com.ymr.mvp.view;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by ymr on 15/7/28.
 */
public interface IActivityView {

    Activity getActivity();

    void gotoActivity(Intent intent);
}
