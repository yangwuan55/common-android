package com.ymr.common.ui.activity;


import android.view.View;

import com.ymr.common.R;
import com.ymr.common.net.DataReceiver;
import com.ymr.common.net.NetWorkController;
import com.ymr.common.net.NetWorkModel;
import com.ymr.common.ui.BaseUIController;
import com.ymr.common.ui.NetWorkUI;
import com.ymr.common.ui.NetWorkUIController;

/**
 * Created by ymr on 15/6/13.
 */
public abstract class BaseNetWorkActivity<D,P> extends BaseActivity implements DataReceiver<D>,NetWorkUI<D,P> {

    private NetWorkUIController mNetworkUiController;

    protected void updateData(P params) {
        mNetworkUiController.updateData(params);
    }

    @Override
    public void onFinishCreateView() {
        initViews();
    }

    @Override
    public int getTitleRightViewId() {
        return 0;
    }

    @Override
    public View.OnClickListener getTitleRightViewOnClickListener() {
        return null;
    }

    protected abstract void initViews();

    protected abstract NetWorkModel<D,P> getNetWorkModel();

    @Override
    public BaseUIController getController() {
        mNetworkUiController = new NetWorkUIController(this);
        return mNetworkUiController;
    }

    @Override
    public int getLoadFailViewId() {
        return R.layout.default_load_fail;
    }

    @Override
    public int getNetErorrViewId() {
        return R.layout.default_net_error;
    }

    @Override
    public NetWorkController<D, P> getNetWorkController() {
        return new NetWorkController<>(this,getNetWorkModel());
    }
}
