package com.ymr.common.ui.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymr on 15/9/22.
 */
public abstract class DataBaseAdapter<D> extends BaseAdapter {

    private final Context mContext;
    private List<D> mDatas = new ArrayList<>();

    public DataBaseAdapter(Context context) {
        mContext = context;
    }

    public DataBaseAdapter(Context context, List<D> datas) {
        mContext = context;
        addDatas(datas);
    }

    public void addDatas(List<D> datas) {
        if (datas != null) {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void setDatas(List<D> datas) {
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas.clear();
        }
        notifyDataSetChanged();
    }

    public List<D> getDatas() {
        return mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public D getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Context getContext() {
        return mContext;
    }
}
