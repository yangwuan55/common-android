package com.ymr.common;

/**
 * Created by ymr on 15/7/27.
 */
public interface IObserveAbleModel {

    interface Listener {
        void onChange();
    }

    void registeListener(Listener listener);

    void unregisteListener(Listener listener);

    void notifyListeners();
}
