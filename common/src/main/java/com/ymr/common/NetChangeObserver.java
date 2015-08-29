package com.ymr.common;

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
	private List<OnNetChangeListener> onNetChangeListeners = new ArrayList<OnNetChangeListener>();

	public static NetChangeObserver getSingleton() {
		return newInstance;
	}

	public void netConnect() {
		Iterator<OnNetChangeListener> it = onNetChangeListeners.iterator();

		while (it.hasNext()) {
			it.next().onNetConnect();
		}
	}

	public void netDisconnect() {
		Iterator<OnNetChangeListener> it = onNetChangeListeners.iterator();

		while (it.hasNext()) {
			it.next().onNetDisconnect();
		}
	}

	public void registerOnNetChangeListener(OnNetChangeListener listener) {
		for (Iterator<OnNetChangeListener> it = onNetChangeListeners.iterator(); it.hasNext();) {
			if (it.next().getClass().equals(listener.getClass())) {
				it.remove();
			}
		}

		this.onNetChangeListeners.add(listener);
	}

	public void unRegisterOnNetChangeListener(OnNetChangeListener listener) {
		this.onNetChangeListeners.remove(listener);
	}

	public static interface OnNetChangeListener {
		/**
		 * 网络断开
		 */
		public void onNetDisconnect();

		/**
		 * 网络连接
		 */
		public void onNetConnect();
	}
}
