package com.ymr.common.net;

import android.content.Context;

import com.android.volley.VolleyError;
import com.ymr.common.SimpleModel;
import com.ymr.common.bean.ApiBase;
import com.ymr.common.net.params.NetRequestParams;
import com.ymr.common.net.volley.VolleyUtil;

import java.util.Map;

/**
 * Created by ymr on 15/6/12.
 */
public abstract class SimpleNetWorkModel<T,P extends NetRequestParams> extends SimpleModel implements NetWorkModel<T, P> {

    private final Context mContext;

    public SimpleNetWorkModel(Context context) {
        mContext = context;
    }

    @Override
    public void updateDatas(P params, final UpdateListener<T> listener) {
        VolleyUtil.getsInstance(mContext).addRequest(params, getApiClass(), new VolleyUtil.RequestListner<ApiBase<T>>() {
            @Override
            public void onSuccess(ApiBase<T> data) {
                if (data.getCode() == 0) {
                    listener.finishUpdate(data.getResult());
                } else {
                    if (data.getResult() != null) {
                        listener.onError(((Map) data.getResult()).get("result") + "");
                    } else {
                        listener.onError(data.getMsg());
                    }
                }
            }

            @Override
            public void onFail(VolleyError error) {
                listener.onError(error.getMessage());
            }
        });
    }

    /**
     * get the class who is child of ApiBase for this model
     * @return
     */
    protected abstract Class getApiClass();

    public Context getContext() {
        return mContext;
    }
}