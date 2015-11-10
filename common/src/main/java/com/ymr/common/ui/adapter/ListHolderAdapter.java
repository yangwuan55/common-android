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

        private int mItemViewType;

        protected abstract void reset();

        protected abstract int getViewId();

        protected abstract void onViewCreate(View view);

        protected View inflate() {
            View inflate = View.inflate(getContext(), getViewId(), null);
            onViewCreate(inflate);
            return inflate;
        }

        void setItemViewType(int viewType) {
            mItemViewType = viewType;
        }

        int getItemViewType() {
            return mItemViewType;
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
        if (verifyConvertView(convertView,position)) {
            v = convertView;
            viewHolder = (ViewHolder<D>) v.getTag();
            viewHolder.reset();
        } else {
            int itemViewType = getItemViewType(position);
            viewHolder = createViewHolder(itemViewType);
            viewHolder.setItemViewType(itemViewType);
            v = viewHolder.inflate();
            v.setTag(viewHolder);
        }
        D item = getItem(position);
        viewHolder.onReceiveData(item, position);
        return v;
    }

    private boolean verifyConvertView(View convertView,int position) {
        boolean result = false;
        if (convertView != null) {
            ViewHolder<D> tag = (ViewHolder<D>) convertView.getTag();
            if (tag.getItemViewType() == getItemViewType(position)) {
                result = true;
            }
        }
        return result;
    }

    protected abstract ViewHolder<D> createViewHolder(int viewType);
}
