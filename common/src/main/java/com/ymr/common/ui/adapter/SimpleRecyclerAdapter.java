package com.ymr.common.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by ymr on 15/11/4.
 */
public abstract class SimpleRecyclerAdapter<D> extends RecyclerView.Adapter implements IDataManager<D> {

    private final Context mContext;
    private final DataManager<D> mDataManager;

    public SimpleRecyclerAdapter(Context context) {
        mContext = context;
        mDataManager = new DataManager<>(this);
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    @Override
    public void addDatas(List<D> datas) {
        mDataManager.addDatas(datas);
    }

    @Override
    public void setDatas(List<D> datas) {
        mDataManager.setDatas(datas);
    }

    @Override
    public List<D> getDatas() {
        return mDataManager.getDatas();
    }

    @Override
    public int getCount() {
        return mDataManager.getCount();
    }

    @Override
    public D getItem(int position) {
        return mDataManager.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mDataManager.getItemId(position);
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
