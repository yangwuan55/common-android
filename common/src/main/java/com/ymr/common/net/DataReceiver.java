package com.ymr.common.net;

/**
 * Created by ymr on 15/6/13.
 */
public interface DataReceiver<T> {
    void onReceiveData(T data);
}
