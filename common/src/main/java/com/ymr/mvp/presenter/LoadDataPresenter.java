package com.ymr.mvp.presenter;

import com.ymr.common.net.NetWorkModel;
import com.ymr.common.net.SimpleResultNetWorkModel;
import com.ymr.mvp.view.ILoadDataView;

/**
 * Created by ymr on 15/11/12.
 */
public class LoadDataPresenter<V extends ILoadDataView> extends BaseNetPresenter<V> {

    public LoadDataPresenter(V view) {
        super(view);
    }

    protected <D> NetWorkModel.UpdateListener wrapNetListener(final NetWorkModel.UpdateListener<D> listener) {
        return wrapNetListener(listener,0);
    }

    protected SimpleResultNetWorkModel.SimpleRequestListener wrapNetListener(final SimpleResultNetWorkModel.SimpleRequestListener listener) {
        return wrapNetListener(listener,0);
    }

    protected <D> NetWorkModel.UpdateListener wrapNetListener(final NetWorkModel.UpdateListener<D> listener, long delay) {
        getView().showLoading(delay);
        return new NetWorkModel.UpdateListener<D>() {
            @Override
            public void finishUpdate(D result) {
                listener.finishUpdate(result);
                getView().hideLoading();
            }

            @Override
            public void onError(NetWorkModel.Error error) {
                getView().hideLoading();
                listener.onError(error);
            }
        };
    }

    protected SimpleResultNetWorkModel.SimpleRequestListener wrapNetListener(final SimpleResultNetWorkModel.SimpleRequestListener listener, long delay) {
        getView().showLoading(delay);
        return new SimpleResultNetWorkModel.SimpleRequestListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
                getView().hideLoading();
            }

            @Override
            public void onFail(NetWorkModel.Error error) {
                getView().hideLoading();
                listener.onFail(error);
            }
        };
    }
    
    public boolean backPressed() {
        return false;
    }

    @Override
    public void onNetDisconnect() {
        super.onNetDisconnect();
        getView().hideLoading();
    }
}
