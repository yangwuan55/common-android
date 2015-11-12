package com.ymr.mvp.view;

import android.view.View;

/**
 * Created by ymr on 15/8/13.
 */
public interface IView extends IActivityView {

    void onError(String msg);

    void onMessage(String msg);

    boolean isCurrView();

    boolean exist();

    View getRootView();
}
