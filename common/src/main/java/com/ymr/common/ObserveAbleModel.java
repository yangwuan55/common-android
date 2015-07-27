package com.ymr.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymr on 15/7/27.
 */
public class ObserveAbleModel implements IObserveAbleModel {
    private List<Listener> mListeners = new ArrayList<>();

    @Override
    public void registeListener(Listener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    @Override
    public void unregisteListener(Listener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void notifyListeners() {
        if (!mListeners.isEmpty())
        for (Listener listener : mListeners) {
            listener.onChange();
        }
    }
}
