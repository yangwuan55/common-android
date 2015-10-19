package com.ymr.mvp.presenter;


import com.ymr.common.BaseApplication;
import com.ymr.common.NetChangeObserver;
import com.ymr.mvp.view.INetView;

/**
 * Created by ymr on 15/8/20.
 */
public class BaseNetPresenter<V extends INetView> extends BasePresenter<V> implements NetChangeObserver.OnNetChangeListener {

    public BaseNetPresenter(V view) {
        super(view);
        BaseApplication.getRefWacher().watch(this);
        NetChangeObserver.getSingleton().registerOnNetChangeListener(this);
    }
    @Override
    public void onNetDisconnect() {
        if (getView().exist()) {
            getView().showNoNetWork();
        }
    }

    @Override
    public void onNetConnect() {
        if (getView().exist()) {
            getView().hideNoNetWork();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        NetChangeObserver.getSingleton().unRegisterOnNetChangeListener(this);
    }
}
