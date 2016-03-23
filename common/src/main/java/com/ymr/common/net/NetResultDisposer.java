package com.ymr.common.net;

import android.content.Context;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.ymr.common.bean.IApiBase;
import com.ymr.common.net.params.NetRequestParams;
import com.ymr.common.net.volley.VolleyUtil;
import com.ymr.common.util.LOGGER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ymr on 15/8/21.
 */
public class NetResultDisposer {
    private static final String TAG = "NetResultDisposer";
    /**
     * 缓存数据时间
     */
    public static final int SAVED_TIME = 2000;
    private static Handler sHandler = new Handler();
    private static HashMap<NetRequestParams,List<NetWorkModel.UpdateListener>> sParamListeners = new HashMap<>();
    private static HashMap<NetRequestParams,IApiBase> sSavedDatas = new HashMap<>();
    private static HashMap<NetRequestParams,VolleyError> sSavedVolleyError = new HashMap<>();
    private static final Object sync = new Object();

    /**
     *
     * @param context
     * @param params
     * @param listener
     * @param tClass
     * @param headers
     * @param cookies
     * @param forceFromServer 是否强制从服务端取数据
     * @param <T>
     */
    public static <T> void dispose(Context context, final NetRequestParams params, final NetWorkModel.UpdateListener<T> listener, Class<T> tClass, Map<String, String> headers, String cookies, boolean forceFromServer) {
        synchronized (sync) {
            if (!forceFromServer) {
                //如果不是强制从服务器取数据则符合缓存规则，进入缓存流程
                if (sSavedDatas.containsKey(params)) {
                    //如果缓存了此数据，从缓存中取得数据
                    finishUpdate(sSavedDatas.get(params),params,listener);
                    LOGGER.i(TAG,"return saved data:"+params);
                } else if (sSavedVolleyError.containsKey(params)) {
                    //如果缓存了此错误信息，从缓存中取得错误
                    failUpdate(sSavedVolleyError.get(params),params,listener);
                    LOGGER.i(TAG,"return saved error:"+params);
                } else if (sParamListeners.containsKey(params)) {
                    //如果请求列表中已经存在此请求，将回调对象加入回调对象列表
                    List<NetWorkModel.UpdateListener> updateListeners = sParamListeners.get(params);
                    updateListeners.add(listener);
                    sParamListeners.put(params, updateListeners);
                    LOGGER.i(TAG,"add listener:"+params);
                } else {
                    //真正从服务器取数据
                    updateFromServer(context, params, listener, tClass, headers, cookies);
                }
            } else {
                //直接从服务器取数据
                updateFromServer(context, params, listener, tClass, headers, cookies);
            }
        }
    }

    private static <T> void updateFromServer(Context context, final NetRequestParams params, NetWorkModel.UpdateListener<T> listener, Class<T> tClass, Map<String, String> headers, String cookies) {
        ArrayList<NetWorkModel.UpdateListener> updateListeners = new ArrayList<>();
        updateListeners.add(listener);
        sParamListeners.put(params, updateListeners);
        VolleyUtil.getsInstance(context).addRequest(params, new VolleyUtil.RequestListner<IApiBase<T>>() {
            @Override
            public void onSuccess(final IApiBase<T> data) {
                synchronized (sync) {
                    finishUpdate(data, params);
                    sSavedDatas.put(params,data);
                    sHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            removeMsg(params);
                            LOGGER.i(TAG,"success remove params:"+params);
                        }
                    }, SAVED_TIME);
                }
            }

            @Override
            public void onFail(VolleyError error) {
                synchronized (sync) {
                    failUpdate(error, params);
                    sSavedVolleyError.put(params,error);
                    sHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            removeMsg(params);
                            LOGGER.i(TAG,"fail    remove params:"+params);
                        }
                    }, SAVED_TIME);
                }
            }
        }, tClass,headers,cookies);
    }

    private static void removeMsg(NetRequestParams params) {
        sSavedDatas.remove(params);
        sSavedVolleyError.remove(params);
        sParamListeners.remove(params);
    }

    private static void failUpdate(VolleyError error, NetRequestParams params) {
        Throwable cause = error.getCause();
        NetWorkModel.Error netError = new NetWorkModel.Error();
        netError.setNetRequestParams(params);
        if (cause != null) {
            netError.setMsg(cause.toString());
            cause.printStackTrace();
        } else {
            LOGGER.e(TAG,"ERROR = " + error);
            netError.setMsg("server error 2");
        }
        List<NetWorkModel.UpdateListener> updateListeners = sParamListeners.get(params);
        for (NetWorkModel.UpdateListener listener : updateListeners) {
            listener.onError(netError);
        }
    }

    private static void failUpdate(VolleyError error, NetRequestParams params, NetWorkModel.UpdateListener listener) {
        Throwable cause = error.getCause();
        NetWorkModel.Error netError = new NetWorkModel.Error();
        netError.setNetRequestParams(params);
        if (cause != null) {
            netError.setMsg(cause.toString());
            cause.printStackTrace();
        } else {
            LOGGER.e(TAG, "ERROR = " + error);
            netError.setMsg("server error 2");
        }
        listener.onError(netError);
    }

    private static <T> void finishUpdate(IApiBase<T> data, NetRequestParams params) {
        List<NetWorkModel.UpdateListener> updateListeners = sParamListeners.get(params);
        NetWorkModel.Error error = new NetWorkModel.Error();
        error.setNetRequestParams(params);
        if (data != null) {
            if (data.getCode() == data.getSuccessCode()) {
                for (NetWorkModel.UpdateListener listener : updateListeners) {
                    listener.finishUpdate(data.getData());
                }
            } else {
                error.setErrorCode(data.getCode());
                if (data.getData() != null && data.getData() instanceof Map) {
                    error.setMsg(data.getMsg());
                } else {
                    error.setMsg(data.getMsg());
                }
                for (NetWorkModel.UpdateListener listener : updateListeners) {
                    listener.onError(error);
                }
            }
        } else {
            error.setMsg("server error 1");
            for (NetWorkModel.UpdateListener listener : updateListeners) {
                listener.onError(error);
            }
        }
    }

    private static <T> void finishUpdate(IApiBase<T> data, NetRequestParams params, NetWorkModel.UpdateListener<T> listener) {
        NetWorkModel.Error error = new NetWorkModel.Error();
        error.setNetRequestParams(params);
        if (data != null) {
            if (data.getCode() == data.getSuccessCode()) {
                listener.finishUpdate(data.getData());
            } else {
                error.setErrorCode(data.getCode());
                if (data.getData() != null && data.getData() instanceof Map) {
                    error.setMsg(data.getMsg());
                } else {
                    error.setMsg(data.getMsg());
                }
                listener.onError(error);
            }
        } else {
            error.setMsg("server error 1");
            listener.onError(error);
        }
    }
}
