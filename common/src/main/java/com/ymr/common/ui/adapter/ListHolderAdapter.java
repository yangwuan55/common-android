package com.ymr.common.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ymr.common.net.DataReceiver;

import java.util.List;

/**
 * Created by ymr on 15/9/22.
 */
public abstract class ListHolderAdapter<D> extends DataBaseAdapter<D> {
    public abstract class ViewHolder<D> {

        protected abstract void reset();

        protected abstract int getViewId(int position);

        protected abstract void onViewCreate(View view);

        protected View inflate(int position) {
            View inflate = View.inflate(getContext(), getViewId(position), null);
            onViewCreate(inflate);
            return inflate;
        }

        public abstract void onReceiveData(D item, int position);
    }

    public ListHolderAdapter(Context context) {
        super(context);
    }

    public ListHolderAdapter(Context context, List<D> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        ViewHolder<D> viewHolder = null;
        if (convertView != null) {
            v = convertView;
            viewHolder = (ViewHolder<D>) v.getTag();
            viewHolder.reset();
        } else {
            viewHolder = createViewHolder(position);
            v = viewHolder.inflate(position);
            v.setTag(viewHolder);
        }
        D item = getItem(position);
        if (item != null) {
            viewHolder.onReceiveData(item,position);
        } else {
            v.setVisibility(View.GONE);
        }
        return v;
    }

    protected abstract ViewHolder<D> createViewHolder(int position);
}
