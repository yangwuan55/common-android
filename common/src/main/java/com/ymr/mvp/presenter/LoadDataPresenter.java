package com.ymr.mvp.presenter;

import com.ymr.common.net.NetWorkModel;
import com.ymr.common.net.SimpleResultNetWorkModel;
import com.ymr.common.util.KeyBoardUtil;
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
        if (isCurrView()) {
            getView().showLoading(delay);
        }
        return new NetWorkModel.UpdateListener<D>() {
            @Override
            public void finishUpdate(D result) {
                listener.finishUpdate(result);
                if (isCurrView()) {
                    getView().hideLoading();
                }
            }

            @Override
            public void onError(NetWorkModel.Error error) {
                if (isCurrView()) {
                    getView().hideLoading();
                }
                listener.onError(error);
            }
        };
    }

    protected SimpleResultNetWorkModel.SimpleRequestListener wrapNetListener(final SimpleResultNetWorkModel.SimpleRequestListener listener, long delay) {
        if (isCurrView()) {
            getView().showLoading(delay);
        }
        return new SimpleResultNetWorkModel.SimpleRequestListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
                if (isCurrView()) {
                    getView().hideLoading();
                }
            }

            @Override
            public void onFail(NetWorkModel.Error error) {
                if (isCurrView()) {
                    getView().hideLoading();
                }
                listener.onFail(error);
            }
        };
    }
    
    public boolean backPressed() {
        KeyBoardUtil.hideSoftKeyboard(getView().getActivity());
        return false;
    }

    @Override
    public void onNetDisconnect() {
        super.onNetDisconnect();
        if (isCurrView()) {
            getView().hideLoading();
        }
    }

    public boolean isCurrView() {
        return getView() != null && getView().isCurrView();
    }
}
