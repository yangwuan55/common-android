package com.ymr.mvp.model;

import android.content.Context;

import com.ymr.common.net.SimpleNetWorkModel;
import com.ymr.common.net.params.NetRequestParams;
import com.ymr.common.util.DeviceInfoUtils;

/**
 * Created by ymr on 15/9/11.
 */
public class CachedSimpleNetworkModel<D> extends SimpleNetWorkModel<D> implements ICachedModel<D> {

    private final CachedModel<D> mCachedModel;

    public CachedSimpleNetworkModel(Context context, final Class<D> dClass,String fileName) {
        super(context, dClass);
        mCachedModel = new CachedModel<D>(context){
            @Override
            protected Class<D> getCachedClass() {
                return dClass;
            }
        };
        mCachedModel.setFileName(fileName);
    }

    @Override
    public void updateDatas(NetRequestParams params, final UpdateListener<D> listener) {
        if (DeviceInfoUtils.hasInternet(getContext())) {
            super.updateDatas(params, new UpdateListener<D>() {
                @Override
                public void finishUpdate(D result) {
                    cacheDatas(result);
                    if (listener != null) {
                        listener.finishUpdate(result);
                    }
                }

                @Override
                public void onError(Error error) {
                    if (listener != null) {
                        listener.onError(error);
                    }
                }
            });
        } else {
            if (listener != null) {
                listener.finishUpdate(getCacheDatas());
            }
        }
    }

    @Override
    public void cacheDatas(D data) {
        mCachedModel.cacheDatas(data);
        notifyListeners();
    }

    @Override
    public D getCacheDatas() {
        return mCachedModel.getCacheDatas();
    }
}
