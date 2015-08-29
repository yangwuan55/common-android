package com.ymr.mvp.model;

import android.content.Context;

import com.ymr.common.util.FileUtil;

import java.util.List;

/**
 * Created by ymr on 15/8/24.
 */
public abstract class CachedModel<D> implements ICachedModel<D> {

    private String mFileName;
    private final Context mContext;

    public CachedModel(Context context) {
        mContext = context;
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    @Override
    public void cacheDatas(List<D> studentlist) {
        FileUtil.writeListToFile(mContext, studentlist, mFileName);
    }

    @Override
    public List<D> getCacheDatas() {
        return FileUtil.getListFromFile(mContext, mFileName, getCachedArrayClass());
    }

    protected abstract Class<D[]> getCachedArrayClass();
}
