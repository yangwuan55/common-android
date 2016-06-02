package com.ymr.common.ui.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.os.Handler;

import com.ymr.common.ui.DataBindingActivityUI;
import com.ymr.common.ui.DataBindingUIController;
import com.ymr.common.ui.IBaseUIController;
import com.ymr.common.util.KeyBoardUtil;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBindingUIController = null;
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
}
