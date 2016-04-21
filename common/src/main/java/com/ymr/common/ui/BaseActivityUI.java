package com.ymr.common.ui;

import android.view.View;

/**
 * Created by ymr on 15/10/13.
 */
public interface BaseActivityUI extends BaseUI{
    boolean hasBack();

    int getTitleRightViewId();

    String getTitleText();

    View.OnClickListener getTitleRightViewOnClickListener();

    boolean isActionBarVisible();

    boolean isTitleBarDiverVisiable();

    boolean onActionbarBackPressed();

    BaseUIController.BaseUIParams getBaseUIParams();

    IBaseUIController getController();

    View getRightView();
}
