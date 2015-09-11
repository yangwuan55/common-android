package com.ymr.mvp.model;

import android.content.Context;
import android.text.TextUtils;

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
    public void cacheDatas(D data) {
        if (!TextUtils.isEmpty(mFileName)) {
            if (data != null) {
                FileUtil.writeBeanToFile(mContext, mFileName, data);
            } else {
                FileUtil.deleteByName(mContext,mFileName);
            }
        }
    }

    @Override
    public D getCacheDatas() {
        if (!TextUtils.isEmpty(mFileName)) {
            return FileUtil.readBeanFromFile(mContext, mFileName, getCachedClass());
        }
        return null;
    }

    public Context getContext() {
        return mContext;
    }

    public String getFileName() {
        return mFileName;
    }

    protected abstract Class<D> getCachedClass();
}
