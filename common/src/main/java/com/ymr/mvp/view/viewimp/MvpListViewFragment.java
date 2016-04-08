package com.ymr.mvp.view.viewimp;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.ymr.common.R;
import com.ymr.common.databinding.LayoutListContainerBinding;
import com.ymr.common.ui.adapter.DataBaseAdapter;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.presenter.ListPresenterHasEmpty;
import com.ymr.mvp.view.IListViewHasEmptyView;

import java.util.List;

/**
 * Created by ymr on 15/10/17.
 */
public abstract class MvpListViewFragment<D, E extends IListItemBean<D>,P extends ListPresenterHasEmpty<D,E,? extends IListViewHasEmptyView<D,E>>> extends LoadDataFragmentView<P> implements IListViewHasEmptyView<D,E> {

    private LayoutListContainerBinding mListDataBinding;
    private DataBaseAdapter<D> mAdapter;

    @Override
    protected void finishCreatePresenter(ViewDataBinding databinding) {
        mListDataBinding = createListContainer();
        Drawable backGround = getBackGround();
        if (backGround != null) {
            mListDataBinding.getRoot().setBackgroundDrawable(backGround);
        }
        mListDataBinding.list.setMode(PullToRefreshBase.Mode.BOTH);
        mListDataBinding.list.getRefreshableView().setDivider(null);
        mListDataBinding.list.getRefreshableView().setSelector(android.R.color.transparent);
        mListDataBinding.list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getPresenter().onRefreshFromTop();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getPresenter().onRefreshFromBottom();
            }
        });
        mListDataBinding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MvpListViewFragment.this.onItemClick(parent, view, position, id);
            }
        });
        mAdapter = getAdapter();
        mListDataBinding.list.setAdapter(mAdapter);
        mListDataBinding.emptyView.addView(getEmptyView());
        ((ViewGroup) databinding.getRoot()).addView(mListDataBinding.getRoot(),0);
        getPresenter().loadDatas();
    }

    protected LayoutListContainerBinding createListContainer() {
        return DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.layout_list_container,null,false);
    }

    protected abstract Drawable getBackGround();

    protected abstract View getEmptyView();

    protected abstract DataBaseAdapter<D> getAdapter();

    protected abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);

    @Override
    public void setDatas(List<D> datas) {
        mAdapter.setDatas(datas);
    }

    @Override
    public void addDatas(List<D> datas) {
        mAdapter.addDatas(datas);
    }

    @Override
    public void setBottomRefreshEnable(boolean enable) {
        if (enable) {
            mListDataBinding.list.setMode(PullToRefreshBase.Mode.BOTH);
        } else {
            mListDataBinding.list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
    }

    @Override
    public void compliteRefresh() {
        mListDataBinding.list.onRefreshComplete();
    }

    @Override
    public void setRefreshEnable(boolean enable) {
        mListDataBinding.list.setEnabled(enable);
    }

    @Override
    public void showEmptyView() {
        mListDataBinding.emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        mListDataBinding.emptyView.setVisibility(View.GONE);
    }

    @Override
    public List<D> getCurrDatas() {
        return mAdapter.getDatas();
    }

    @Override
    public void startRefresh() {
    }

    @Override
    public void finishRefresh() {
    }
}
