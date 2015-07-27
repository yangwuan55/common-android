package com.ymr.common.net;

import com.ymr.common.IModel;
import com.ymr.common.net.params.NetRequestParams;

/**
 * Created by ymr on 15/5/14.
 */
public interface NetWorkModel<T> extends IModel {

    void updateDatas(NetRequestParams params, UpdateListener<T> listener);

    interface UpdateListener<T> {
        void finishUpdate(T result);
        void onError(String msg);
    }

    interface NetworkChangedListener {
        void onNetworkChange();
    }
}
