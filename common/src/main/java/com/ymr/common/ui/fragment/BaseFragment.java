package com.ymr.common.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ymr.common.ui.view.BaseFragmentUI;
import com.ymr.common.util.StatisticalHelper;

/**
 * Created by ymr on 15/10/13.
 */
public abstract class BaseFragment extends Fragment implements BaseFragmentUI {

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onStartCreatView();
        mRootView = inflateView(inflater, container, savedInstanceState);
        onFinishCreateView();
        return mRootView;
    }

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getContentViewId(),container,false);
    }

    @Override
    public void onStartCreatView() {

    }

    @Override
    public boolean isResume() {
        return isResumed();
    }

    @Override
    public void writeToStatistical(String actionType) {
        StatisticalHelper.doStatistical(getActivity(), actionType);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }
}
