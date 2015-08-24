package com.ymr.common.net;

import android.content.Context;

import com.ymr.common.SimpleModel;
import com.ymr.common.net.params.NetRequestParams;

/**
 * Created by ymr on 15/6/12.
 */
public class SimpleNetWorkModel<T> extends SimpleModel implements NetWorkModel<T> {

    private final Context mContext;
    private final Class<T> mTClass;

    public SimpleNetWorkModel(Context context, Class<T> tClass) {
        mContext = context;
        mTClass = tClass;
    }

    @Override
    public void updateDatas(NetRequestParams params, final UpdateListener<T> listener) {
        NetResultDisposer.dispose(mContext, params, listener, mTClass);
    }

    public Context getContext() {
        return mContext;
    }
}