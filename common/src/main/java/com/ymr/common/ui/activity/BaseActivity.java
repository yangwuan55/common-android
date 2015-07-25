package com.ymr.common.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ymr.common.ui.BaseUI;
import com.ymr.common.ui.BaseUIController;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ymr on 15/6/25.
 */
public abstract class BaseActivity extends Activity implements BaseUI {
    private BaseUIController mBaseUIController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseUIController = getController();
        mBaseUIController.initActivity();
    }

    public void setTitle(String title) {
        mBaseUIController.setTitle(title);
    }

    @Override
    public void onStartCreatView() {

    }

    @Override
    public BaseUIController.BaseUIParams getBaseUIParams() {
        return null;
    }

    @Override
    public int getTitleRightViewId() {
        return 0;
    }

    @Override
    public View.OnClickListener getTitleRightViewOnClickListener() {
        return null;
    }

    @Override
    public BaseUIController getController() {
        return new BaseUIController(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
