package com.ymr.mvp.view.viewimp;

import android.app.Activity;
import android.content.Intent;

import com.ymr.common.ui.activity.BaseActivity;
import com.ymr.mvp.view.IView;

/**
 * Created by ymr on 15/9/16.
 */
public abstract class BaseActivityView extends BaseActivity implements IView {

    @Override
    public boolean isCurrView() {
        return isResume();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void gotoActivity(Intent intent) {
        startActivity(intent);
    }
}
