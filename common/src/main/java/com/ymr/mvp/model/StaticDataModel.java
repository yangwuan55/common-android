package com.ymr.mvp.model;

import android.content.Context;

import com.ymr.common.net.NetWorkModel;
import com.ymr.common.net.params.NetRequestParams;

/**
 * Created by ymr on 15/11/3.
 */
public abstract class StaticDataModel<D> extends CachedSimpleNetworkModel<D> {
    public StaticDataModel(Context context, Class<D> dClass, String fileName) {
        super(context, dClass, fileName);
    }

    public void initDatas() {
        updateDatas(getParams(), null);
    }

    @Override
    public void cacheDatas(D data) {
        super.cacheDatas(data);
        notifyListeners();
    }

    protected abstract NetRequestParams getParams();
}
