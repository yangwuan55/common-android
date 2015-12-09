package com.ymr.common.ui.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by ymr on 15/9/22.
 */
public abstract class DataBaseAdapter<D> extends BaseAdapter implements IDataManager<D> {

    private final Context mContext;
    private IDataManager<D> mDataManagerDelegate;

    public DataBaseAdapter(Context context) {
        mContext = context;
        mDataManagerDelegate = new DataManagerDelegate<>(this);
    }

    public DataBaseAdapter(Context context, List<D> datas) {
        mContext = context;
        mDataManagerDelegate = new DataManagerDelegate<>(this);
        addDatas(datas);
    }

    @Override
    public void addDatas(List<D> datas) {
        mDataManagerDelegate.addDatas(datas);
    }

    @Override
    public void setDatas(List<D> datas) {
        mDataManagerDelegate.setDatas(datas);
    }

    @Override
    public List<D> getDatas() {
        return mDataManagerDelegate.getDatas();
    }

    @Override
    public int getCount() {
        return mDataManagerDelegate.getCount();
    }

    @Override
    public D getItem(int position) {
        return mDataManagerDelegate.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mDataManagerDelegate.getItemId(position);
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
