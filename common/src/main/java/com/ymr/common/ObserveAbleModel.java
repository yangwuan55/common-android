package com.ymr.common;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ymr on 15/7/27.
 */
public class ObserveAbleModel implements IObserveAbleModel {
    private List<WeakReference<Listener>> mListeners = new ArrayList<>();

    @Override
    public void registeListener(Listener listener) {
        if (!contains(listener)) {
            mListeners.add(new WeakReference<Listener>(listener));
        }
    }

    private boolean contains(Listener listener) {
        if (!mListeners.isEmpty()) {
            for (WeakReference<Listener> weakReference : mListeners) {
                Listener referenceListener = weakReference.get();
                if (referenceListener != null && referenceListener.equals(listener)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void unregisteListener(Listener listener) {
        Iterator<WeakReference<Listener>> iterator = mListeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<Listener> next = iterator.next();
            Listener referenceListener = next.get();
            if (referenceListener != null && referenceListener.equals(listener)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void notifyListeners() {
        if (!mListeners.isEmpty())
        for (WeakReference<Listener> weakReference : mListeners) {
            Listener listener = weakReference.get();
            listener.onChange();
        }
    }
}
