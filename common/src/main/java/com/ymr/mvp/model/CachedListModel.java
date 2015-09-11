package com.ymr.mvp.model;

import android.content.Context;

import com.ymr.common.util.FileUtil;
import com.ymr.mvp.model.bean.ICachedListModel;

import java.util.List;

/**
 * Created by ymr on 15/9/11.
 */
public abstract class CachedListModel<D> extends CachedModel<List<D>> implements ICachedListModel<D> {

    public CachedListModel(Context context) {
        super(context);
    }

    @Override
    public void cacheDatas(List<D> data) {
        FileUtil.writeListToFile(getContext(), data, getFileName());
    }

    @Override
    public List<D> getCacheDatas() {
        return FileUtil.getListFromFile(getContext(),getFileName(),getCachedArrayClass());
    }

    protected abstract Class<D[]> getCachedArrayClass();

    @Deprecated
    @Override
    protected Class<List<D>> getCachedClass() {
        return null;
    }

}
