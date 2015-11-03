package com.ymr.common.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import java.util.List;

/**
 * Created by ymr on 15/10/13.
 */
public abstract class ListDataBindingAdapter<D> extends ListHolderAdapter<D> {

    public abstract class DataBindingViewHolder extends ViewHolder<D> {

        private ViewDataBinding mDataBinding;

        @Deprecated
        @Override
        protected void onViewCreate(View view) {

        }

        @Override
        protected View inflate(int position) {
            mDataBinding = DataBindingUtil.inflate(((Activity) getContext()).getLayoutInflater(), getViewId(position), null, false);
            return mDataBinding.getRoot();
        }

        @Override
        public void onReceiveData(D data,int position) {
            onReceiveData(data, mDataBinding,position);
        }

        @Override
        protected void reset() {
            reset(mDataBinding);
        }

        protected abstract void reset(ViewDataBinding mDataBinding);

        public ViewDataBinding getDataBinding() {
            return mDataBinding;
        }

        protected abstract void onReceiveData(D data, ViewDataBinding dataBinding,int position);
    }

    public ListDataBindingAdapter(Context context) {
        super(context);
    }

    public ListDataBindingAdapter(Context context, List<D> datas) {
        super(context, datas);
    }

    @Override
    protected ViewHolder<D> createViewHolder(int position) {
        return createDataBindingViewHolder();
    }

    protected abstract DataBindingViewHolder createDataBindingViewHolder();
}
