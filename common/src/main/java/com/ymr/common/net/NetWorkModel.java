package com.ymr.common.net;

import com.android.volley.VolleyError;

/**
 * Created by ymr on 15/5/14.
 */
public interface NetWorkModel<T,P> {

    void updateDatas(P params, UpdateListener<T> listener);

    interface UpdateListener<T> {
        void finishUpdate(T result);
        void onError(String msg);
        void onError(VolleyError error);
    }

    interface NetworkChangedListener {
        void onNetworkChange();
    }
}
