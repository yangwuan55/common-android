package com.ymr.mvp.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;

/**
 * Created by ymr on 16/4/11.
 */
public interface DataBindingViewGetter {
    int getViewLayout();
    void createDataBinding(ViewDataBinding viewDataBinding);

    class Util {
        public static ViewDataBinding getViewDataBinding(DataBindingViewGetter emptyViewLayout, Context context) {
            ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), emptyViewLayout.getViewLayout(), null, false);
            emptyViewLayout.createDataBinding(viewDataBinding);
            return viewDataBinding;
        }
    }
}
