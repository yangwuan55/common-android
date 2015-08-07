package com.ymr.common.net;

import android.content.Context;

import com.android.volley.VolleyError;
import com.ymr.common.SimpleModel;
import com.ymr.common.bean.ApiBase;
import com.ymr.common.net.params.NetRequestParams;
import com.ymr.common.net.volley.VolleyUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by ymr on 15/6/12.
 */
public class SimpleNetWorkModel<T> extends SimpleModel implements NetWorkModel<T> {

    private final Context mContext;
    private final Class<T> mTClass;

    public SimpleNetWorkModel(Context context,Class<T> tClass) {
        mContext = context;
        mTClass = tClass;
    }

    @Override
    public void updateDatas(NetRequestParams params, final UpdateListener<T> listener) {
        VolleyUtil.getsInstance(mContext).addRequest(params,new VolleyUtil.RequestListner<ApiBase<T>>() {
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
                    listener.onError("server error");
                }
            }

            @Override
            public void onFail(VolleyError error) {
                listener.onError(error.getCause().toString());
            }
        }, mTClass);
    }

    public Context getContext() {
        return mContext;
    }
}