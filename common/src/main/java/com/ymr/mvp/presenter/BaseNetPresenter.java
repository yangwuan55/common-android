package com.ymr.mvp.presenter;


import com.ymr.common.BaseApplication;
import com.ymr.common.NetChangeObserver;
import com.ymr.mvp.view.INetView;

/**
 * Created by ymr on 15/8/20.
 */
public class BaseNetPresenter extends BasePresenter implements NetChangeObserver.OnNetChangeListener {

    private final INetView mView;

    public BaseNetPresenter(INetView view) {
        super(view);
        BaseApplication.getRefWacher().watch(this);
        mView = view;
        NetChangeObserver.getSingleton().registerOnNetChangeListener(this);
    }
    @Override
    public void onNetDisconnect() {
        if (mView.isCurrView()) {
            mView.showNoNetWork();
        }
    }

    @Override
    public void onNetConnect() {
        if (mView.isCurrView()) {
            mView.hideNoNetWork();
        }
    }

    public void onDestroy() {
        NetChangeObserver.getSingleton().unRegisterOnNetChangeListener(this);
    }
}
