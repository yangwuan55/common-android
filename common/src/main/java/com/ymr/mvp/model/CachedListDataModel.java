package com.ymr.mvp.model;

import android.content.Context;


import com.ymr.mvp.model.bean.IListItemBean;

import java.util.List;

/**
 * Created by ymr on 15/8/21.
 */
public abstract class CachedListDataModel<D, E extends IListItemBean<D>> extends ListDataModel<D,E> implements ICachedListDataModel<D, E> {

    private final Context mContext;
    private final CachedListModel<D> mCachedModel;

    public CachedListDataModel(Context context, Class<E> eClass) {
        super(context, eClass);
        mContext = context;
        mCachedModel = new CachedListModel<D>(mContext) {
            @Override
            protected Class<D[]> getCachedArrayClass() {
                return CachedListDataModel.this.getCachedArrayClass();
            }
        };
    }

    @Override
    public void cacheDatas(List<D> data) {
        mCachedModel.cacheDatas(data);
    }

    @Override
    public List<D> getCacheDatas() {
        return mCachedModel.getCacheDatas();
    }

    protected abstract Class<D[]> getCachedArrayClass();

    @Override
    public void setCacheName(String cacheName) {
        mCachedModel.setFileName(cacheName);
    }
}
