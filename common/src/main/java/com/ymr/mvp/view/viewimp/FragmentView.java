package com.ymr.mvp.view.viewimp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ymr.mvp.view.IView;

/**
 * Created by ymr on 15/9/16.
 */
public abstract class FragmentView<T extends Fragment & IView> implements IView {

    private final T mView;

    public FragmentView(T iView) {
        mView = iView;
    }

    @Override
    public boolean isCurrView() {
        return mView.isVisible();
    }

    @Override
    public boolean exist() {
        return !mView.isDetached() && mView.getActivity() != null;
    }

    @Override
    public Activity getActivity() {
        return mView.getActivity();
    }

    @Override
    public void gotoActivity(Intent intent) {
        mView.startActivity(intent);
    }

    @Override
    public View getRootView() {
        return mView.getRootView();
    }
}
