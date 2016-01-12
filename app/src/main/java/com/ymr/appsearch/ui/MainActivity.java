package com.ymr.appsearch.ui;

import android.app.ProgressDialog;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.view.View;

import com.ymr.appsearch.R;
import com.ymr.appsearch.databinding.LayoutMainBinding;
import com.ymr.appsearch.presenter.AppPresenter;
import com.ymr.appsearch.ui.viewinterface.IAppView;
import com.ymr.common.util.LOGGER;
import com.ymr.mvp.view.viewimp.DataBindingActivityView;
import com.ymr.mvp.view.viewimp.LoadDataActivityView;

public class MainActivity extends LoadDataActivityView<AppPresenter> implements IAppView, View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static final int TIME_OUT = 60000;
    private LayoutMainBinding mDatabinding;
    private ProgressDialog mProgressDialog;
    private Handler mHandler = new Handler();

    @Override
    public boolean hasBack() {
        return false;
    }

    @Override
    public String getTitleText() {
        return "t9";
    }

    @Override
    public int getContentViewId() {
        return R.layout.layout_main;
    }

    @Override
    protected void finishCreatePresenter(ViewDataBinding databinding) {
        setTimeOut(TIME_OUT);
        mDatabinding = ((LayoutMainBinding) databinding);
        mDatabinding.appContainer.setAdapter(getPresenter().getAdapter());
        mDatabinding.appContainer.setOnItemClickListener(getPresenter().getItemClick());
        setListeners();
        getPresenter().loadDatas();
    }

    @Override
    protected AppPresenter onCreatePresenter() {
        AppPresenter appPresenter = new AppPresenter(this);
        return appPresenter;
    }

    private void setListeners() {
        mDatabinding.bt0.setOnClickListener(this);
        mDatabinding.bt1.setOnClickListener(this);
        mDatabinding.bt2.setOnClickListener(this);
        mDatabinding.bt3.setOnClickListener(this);
        mDatabinding.bt4.setOnClickListener(this);
        mDatabinding.bt5.setOnClickListener(this);
        mDatabinding.bt6.setOnClickListener(this);
        mDatabinding.bt7.setOnClickListener(this);
        mDatabinding.bt8.setOnClickListener(this);
        mDatabinding.bt9.setOnClickListener(this);
        mDatabinding.btXing.setOnClickListener(this);
        mDatabinding.btDelete.setOnClickListener(this);
        mDatabinding.btDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getPresenter().longDelete();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_0:
            case R.id.bt_1:
            case R.id.bt_2:
            case R.id.bt_3:
            case R.id.bt_4:
            case R.id.bt_5:
            case R.id.bt_6:
            case R.id.bt_7:
            case R.id.bt_8:
            case R.id.bt_9:
                CharSequence contentDescription = v.getContentDescription();
                getPresenter().clickNum(contentDescription);
                break;
            case R.id.bt_delete:
                getPresenter().delete();
                break;
        }
    }

    @Override
    public void showNoNetWork() {

    }

    @Override
    public void hideNoNetWork() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().onPause();
    }
}
