package com.ymr.mvp.view.viewimp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ymr.mvp.view.IView;

/**
 * Created by ymr on 15/9/16.
 */
public abstract class ViewDelegate implements IView {

    private IView mView;

    public ViewDelegate(IView iView) {
        if (iView instanceof Activity) {
            mView = new ActivityViewDelegate((Activity) iView) {
                @Override
                public void onError(String msg) {
                    ViewDelegate.this.onError(msg);
                }

                @Override
                public void onMessage(String msg) {
                    ViewDelegate.this.onMessage(msg);
                }
            };
        } else if (iView instanceof Fragment){
            mView = new FragmentViewDelegate((Fragment) iView) {
                @Override
                public void onError(String msg) {
                    ViewDelegate.this.onError(msg);
                }

                @Override
                public void onMessage(String msg) {
                    ViewDelegate.this.onMessage(msg);
                }
            };
        }
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
    public Activity getActivity() {
        return mView.getActivity();
    }

    @Override
    public void gotoActivity(Intent intent) {
        mView.gotoActivity(intent);
    }

    @Override
    public View getRootView() {
        return mView.getRootView();
    }
}
