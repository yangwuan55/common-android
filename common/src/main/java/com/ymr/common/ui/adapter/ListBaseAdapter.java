package com.ymr.common.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ymr.common.net.DataReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymr on 15/6/13.
 * D is the data type
 * V is the view who can recieve the data.
 */
public abstract class ListBaseAdapter<D,V extends View & DataReceiver<D>> extends BaseAdapter {

    private final Context mContext;
    private List<D> mDatas = new ArrayList<>();

    public ListBaseAdapter(Context context) {
        mContext = context;
    }

    public ListBaseAdapter(Context context, List<D> datas) {
        mContext = context;
        addDatas(datas);
    }

    public void addDatas(List<D> datas) {
        if (datas != null) {
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void setDatas(List<D> datas) {
        if (datas != null) {
            mDatas = datas;
        }
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V v = null;
        if (convertView != null) {
            v = (V) convertView;
        } else {
            v = initView(mContext);
        }
        D item = getItem(position);
        if (item != null) {
            v.onReceiveData(item);
        } else {
            v.setVisibility(View.GONE);
        }
        return v;
    }

    protected abstract V initView(Context context);
}
