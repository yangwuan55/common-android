package com.ymr.mvp.model;

import android.content.Context;

import com.ymr.common.net.SimpleNetWorkModel;
import com.ymr.common.net.params.NetRequestParams;

/**
 * Created by ymr on 15/9/11.
 */
public abstract class CachedSimpleNetworkModel<D> extends SimpleNetWorkModel<D> implements ICachedModel<D> {

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

    public void initDatas() {
        updateDatas(getParams(), new UpdateListener<D>() {
            @Override
            public void finishUpdate(D result) {
                cacheDatas(result);
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    protected abstract NetRequestParams getParams();

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
