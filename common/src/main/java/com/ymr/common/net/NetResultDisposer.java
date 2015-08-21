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
    public static <T> void dispose(Context context,NetRequestParams params, final NetWorkModel.UpdateListener<T> listener,Class<T> tClass) {
        VolleyUtil.getsInstance(context).addRequest(params,new VolleyUtil.RequestListner<ApiBase<T>>() {
            @Override
            public void onSuccess(ApiBase<T> data) {
                if (data != null) {
                    if (data.getCode() == 0) {
                        listener.finishUpdate(data.getResult());
                    } else {
                        if (data.getResult() != null) {
                            listener.onError(((Map) data.getResult()).get("result") + "");
                        } else {
                            listener.onError(data.getMsg());
                        }
                    }
                } else {
                    listener.onError("server error 1");
                }
            }

            @Override
            public void onFail(VolleyError error) {
                Throwable cause = error.getCause();
                if (cause != null) {
                    listener.onError(cause.toString());
                    cause.printStackTrace();
                } else {
                    listener.onError("server error 2");
                }
            }
        }, tClass);
    }
}
