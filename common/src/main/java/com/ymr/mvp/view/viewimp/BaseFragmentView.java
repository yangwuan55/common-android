package com.ymr.mvp.view.viewimp;

import android.content.Intent;
import android.os.Bundle;

import com.ymr.common.BaseApplication;
import com.ymr.common.ui.fragment.BaseFragment;
import com.ymr.mvp.view.IView;

/**
 * Created by ymr on 15/9/16.
 */
public abstract class BaseFragmentView extends BaseFragment implements IView {

    private BaseView mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getRefWacher().watch(this);
        mView = new BaseView(this) {
            @Override
            public void onError(String msg) {
                BaseFragmentView.this.onError(msg);
            }

            @Override
            public void onMessage(String msg) {
                BaseFragmentView.this.onMessage(msg);
            }
        };
    }

    @Override
    public boolean isCurrView() {
        return mView.isCurrView();
    }

    @Override
    public boolean exist() {
        return mView.exist();
    }

    @Override
    public void gotoActivity(Intent intent) {
        mView.gotoActivity(intent);
    }
}
