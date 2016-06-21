package com.ymr.mvp.view.viewimp;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.ymr.common.ui.activity.BaseDataBindingActivity;
import com.ymr.mvp.view.IView;

/**
 * Created by ymr on 15/10/12.
 */
public abstract class DataBindingActivityView extends BaseDataBindingActivity implements IView,MvpBaseView {
    private ViewDelegate mView;
    private Handler mHandler = new Handler();
    @Override
    public void onFinishCreateView() {
        mView = new ViewDelegate(this) {
            @Override
            public void onError(String msg) {
                DataBindingActivityView.this.onError(msg);
            }

            @Override
            public void onMessage(String msg) {
                DataBindingActivityView.this.onMessage(msg);
            }
        };
    }

    @Override
    public boolean isCurrView() {
        return mView != null && mView.isCurrView();
    }

    @Override
    public boolean exist() {
        return mView != null && mView.exist();
    }

    @Override
    public Activity getActivity() {
        return mView.getActivity();
    }

    @Override
    public void gotoActivity(Intent intent) {
        mView.gotoActivity(intent);
    }

    @Deprecated
    @Override
    public void onInitViews() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mView = null;
            }
        });
    }
}
