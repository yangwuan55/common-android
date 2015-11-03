package com.ymr.mvp.view.viewimp;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ymr.common.ui.DataBindingFragmentUI;

/**
 * Created by ymr on 15/10/13.
 */
public abstract class DataBindingFragmentView extends BaseFragmentView implements DataBindingFragmentUI {

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, getContentViewId(), container, false);
        onCreateDataBinding(dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onFinishCreateView() {

    }
}
