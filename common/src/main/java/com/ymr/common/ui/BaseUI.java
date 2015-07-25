package com.ymr.common.ui;

import android.view.View;

/**
 * Created by ymr on 15/6/25.
 */
public interface BaseUI {

    void onStartCreatView();

    void onFinishCreateView();

    String getTitleText();

    int getContentViewId();

    BaseUIController.BaseUIParams getBaseUIParams();

    boolean hasBack();

    int getTitleRightViewId();

    View.OnClickListener getTitleRightViewOnClickListener();

    BaseUIController getController();
}
