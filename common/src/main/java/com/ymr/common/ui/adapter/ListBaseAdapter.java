package com.ymr.common.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ymr.common.net.DataReceiver;

import java.util.List;

/**
 * Created by ymr on 15/6/13.
 * D is the data type
 * V is the view who can recieve the data.
 */
public abstract class ListBaseAdapter<D,V extends View & ListBaseAdapter.AdapterItem<D>> extends DataBaseAdapter<D> {

    public interface AdapterItem<D> extends DataReceiver<D> {
        void reset();
    }

    public ListBaseAdapter(Context context) {
        super(context);
    }

    public ListBaseAdapter(Context context, List<D> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V v = null;
        if (convertView != null) {
            v = (V) convertView;
            v.reset();
        } else {
            v = initView(getContext());
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
