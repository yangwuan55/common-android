package com.ymr.common;

/**
 * Created by ymr on 15/7/27.
 */
public class SimpleModel implements IModel{

    private final ObserveAbleModel mObserveAbleModel;

    public SimpleModel() {
        mObserveAbleModel = new ObserveAbleModel();
    }

    @Override
    public void registeListener(Listener listener) {
        mObserveAbleModel.registeListener(listener);
    }

    @Override
    public void unregisteListener(Listener listener) {
        mObserveAbleModel.unregisteListener(listener);
    }

    @Override
    public void notifyListeners() {
        mObserveAbleModel.notifyListeners();
    }
}
