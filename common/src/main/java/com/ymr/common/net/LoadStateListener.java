package com.ymr.common.net;

/**
 * Created by ymr on 15/6/18.
 */
public interface LoadStateListener<T> extends DataReceiver<T> {
    void onStateChange(NetworkLoadStatus state);

    enum NetworkLoadStatus {
        LOAD_IDEL,
        LOAD_START,
        LOAD_FINISH,
        LOAD_FAIL,
        LOAD_NETWORK_ERROR,
        LOAD_PARAMS_NULL
    }
}
