package com.ymr.common.ui.activity;

import android.databinding.ViewDataBinding;

import com.ymr.common.ui.DataBindingActivityUI;
import com.ymr.common.ui.DataBindingUIController;
import com.ymr.common.ui.IBaseUIController;

/**
 * Created by ymr on 15/10/12.
 */
public abstract class BaseDataBindingActivity extends BaseFragmentActivity implements DataBindingActivityUI {

    private DataBindingUIController<BaseDataBindingActivity> mDataBindingUIController;

    @Override
    public IBaseUIController getController() {
        mDataBindingUIController = new DataBindingUIController<>(this);
        return mDataBindingUIController;
    }

    public ViewDataBinding getRootDataBinding() {
        return mDataBindingUIController.getDataBinding();
    }
}
