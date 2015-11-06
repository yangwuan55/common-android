package com.ymr.common.net;

import android.content.Context;

import com.ymr.common.net.params.NetRequestParams;

/**
 * Created by ymr on 15/7/9.
 */
public class SimpleResultNetWorkModel extends SimpleNetWorkModel<Void> {

    public interface SimpleRequestListener {
        void onSuccess();
        void onFail(Error error);
    }

    public SimpleResultNetWorkModel(Context context) {
        super(context,null);
    }

    public void sendRequest(NetRequestParams params, final SimpleRequestListener listener) {
        updateDatas(params, new UpdateListener<Void>() {
            @Override
            public void finishUpdate(Void result) {
                listener.onSuccess();
            }

            @Override
            public void onError(Error error) {
                listener.onFail(error);
            }
        });
    }
}
