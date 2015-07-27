package com.ymr.common.net;

import com.ymr.common.IModel;

/**
 * Created by ymr on 15/5/14.
 */
public interface NetWorkModel<T,P> extends IModel {

    void updateDatas(P params, UpdateListener<T> listener);

    interface UpdateListener<T> {
        void finishUpdate(T result);
        void onError(String msg);
    }

    interface NetworkChangedListener {
        void onNetworkChange();
    }
}
