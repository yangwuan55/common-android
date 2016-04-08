package com.ymr.mvp.view.viewimp;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ymr.common.ui.DataBindingFragmentUI;

/**
 * Created by ymr on 15/10/13.
 */
public abstract class DataBindingFragmentView extends BaseFragmentView implements DataBindingFragmentUI {

    private ViewDataBinding mDataBinding;

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mDataBinding == null) {
            mDataBinding = DataBindingUtil.inflate(inflater, getContentViewId(), container, false);
            onCreateDataBinding(mDataBinding);
        }
        ViewGroup parent = (ViewGroup) mDataBinding.getRoot().getParent();
        if (parent != null) {
            parent.removeView(mDataBinding.getRoot());
        }
        return mDataBinding.getRoot();
    }

    @Override
    public void onFinishCreateView() {

    }
}
