package com.ymr.common;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 网络状态观察者
 * 
 * @author Administrator
 *
 */
public class NetChangeObserver {
    private static NetChangeObserver newInstance = new NetChangeObserver();
    private List<WeakReference<OnNetChangeListener>> onNetChangeListeners = new ArrayList<>();

    public static NetChangeObserver getSingleton() {
        return newInstance;
    }

    public void netConnect() {
        Iterator<WeakReference<OnNetChangeListener>> it = onNetChangeListeners.iterator();

        while (it.hasNext()) {
            OnNetChangeListener onNetChangeListener = it.next().get();
            if (onNetChangeListener != null) {
                onNetChangeListener.onNetConnect();
            }
        }
    }

    public void netDisconnect() {
        Iterator<WeakReference<OnNetChangeListener>> it = onNetChangeListeners.iterator();

        while (it.hasNext()) {
            OnNetChangeListener onNetChangeListener = it.next().get();
            if (onNetChangeListener != null) {
                onNetChangeListener.onNetDisconnect();
            }
        }
    }

    public void registerOnNetChangeListener(OnNetChangeListener listener) {
        if (!contains(listener)) {
            this.onNetChangeListeners.add(new WeakReference<OnNetChangeListener>(listener));
        }

    }

    private boolean contains(OnNetChangeListener listener) {
        Iterator<WeakReference<OnNetChangeListener>> iterator = onNetChangeListeners.iterator();
        while (iterator.hasNext()) {
            OnNetChangeListener onNetChangeListener = iterator.next().get();
            if (listener != null && listener.equals(onNetChangeListener)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void unRegisterOnNetChangeListener(OnNetChangeListener listener) {
        Iterator<WeakReference<OnNetChangeListener>> iterator = onNetChangeListeners.iterator();
        while (iterator.hasNext()) {
            OnNetChangeListener onNetChangeListener = iterator.next().get();
            if (listener != null && listener.equals(onNetChangeListener)) {
                iterator.remove();
            }
        }
    }

    public interface OnNetChangeListener {
        /**
         * 网络断开
         */
        void onNetDisconnect();

        /**
         * 网络连接
         */
        void onNetConnect();
    }
}
