package com.ymr.common.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by ymr on 15/11/4.
 */
public abstract class SimpleRecyclerAdapter<D> extends RecyclerView.Adapter implements IDataManager<D> {

    private final Context mContext;
    private final DataManagerDelegate<D> mDataManagerDelegate;

    public SimpleRecyclerAdapter(Context context) {
        mContext = context;
        mDataManagerDelegate = new DataManagerDelegate<>(this);
    }

    @Override
    public int getItemCount() {
        return getCount();
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
