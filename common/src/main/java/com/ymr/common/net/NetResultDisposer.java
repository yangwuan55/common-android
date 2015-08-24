package com.ymr.common.net;

import android.content.Context;

import com.android.volley.VolleyError;
import com.ymr.common.bean.ApiBase;
import com.ymr.common.net.params.NetRequestParams;
import com.ymr.common.net.volley.VolleyUtil;

import java.util.Map;

/**
 * Created by ymr on 15/8/21.
 */
public class NetResultDisposer {
    public static <T> void dispose(Context context, NetRequestParams params, final NetWorkModel.UpdateListener<T> listener, Class<T> tClass) {
        VolleyUtil.getsInstance(context).addRequest(params, new VolleyUtil.RequestListner<ApiBase<T>>() {
            @Override
            public void onSuccess(ApiBase<T> data) {
                NetWorkModel.Error error = new NetWorkModel.Error();
                if (data != null) {
                    if (data.getCode() == 0) {
                        listener.finishUpdate(data.getResult());
                    } else {
                        error.setErrorCode(data.getCode());
                        if (data.getResult() != null) {
                            error.setMsg(((Map) data.getResult()).get("result") + " " + data.getMsg());
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

            @Override
            public void onFail(VolleyError error) {
                Throwable cause = error.getCause();
                NetWorkModel.Error netError = new NetWorkModel.Error();
                if (cause != null) {
                    netError.setMsg(cause.toString());
                    cause.printStackTrace();
                } else {
                    netError.setMsg("server error 2");
                }
                listener.onError(netError);
            }
        }, tClass);
    }
}
