package com.ymr.common.ui;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.widget.FrameLayout;

/**
 * Created by ymr on 15/10/12.
 */
public class DataBindingUIController<T extends Activity & DataBindingActivityUI> extends BaseUIController<T> {

    private ViewDataBinding mRootViewDataBinding;

    public DataBindingUIController(T activity) {
        super(activity);
    }

    @Override
    protected void inflateView(FrameLayout parent) {
        mRootViewDataBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), mActivity.getContentViewId(), parent, true);
    }

    @Override
    protected void onFinishCreateView() {
        super.onFinishCreateView();
        mActivity.onCreateDataBinding(mRootViewDataBinding);
    }

    public ViewDataBinding getDataBinding() {
        return mRootViewDataBinding;
    }
}
