package com.ymr.mvp.view.viewimp;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymr.common.R;
import com.ymr.common.databinding.LayoutListContainerBinding;
import com.ymr.common.ui.adapter.DataBaseAdapter;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.presenter.ListPresenterHasEmpty;
import com.ymr.mvp.view.DataBindingViewGetter;
import com.ymr.mvp.view.IListViewHasEmptyView;
import com.ymr.mvp.view.ILoadDataListView;

import java.util.List;

public class ListViewDelegate<D, E extends IListItemBean<D>, P extends ListPresenterHasEmpty<D, E, ? extends IListViewHasEmptyView<D, E>>> implements IListViewHasEmptyView<D,E> {
    private final ILoadDataListView<D, E, P> mILoadDataListView;
    LayoutListContainerBinding mListDataBinding;
    DataBaseAdapter<D> mAdapter;

    public ListViewDelegate(ILoadDataListView<D, E, P> iLoadDataListView) {
        this.mILoadDataListView = iLoadDataListView;
    }

    protected void finishCreatePresenter(ViewDataBinding databinding) {
        initListViews(databinding);
        mILoadDataListView.getPresenter().loadDatas();
    }

    void initListViews(ViewDataBinding databinding) {
        mListDataBinding = createListContainer();
        Drawable backGround = mILoadDataListView.getBackGround();
        if (backGround != null) {
            mListDataBinding.getRoot().setBackgroundDrawable(backGround);
        }
        mListDataBinding.list.setMode(mILoadDataListView.getFreshMode());
        mListDataBinding.list.getRefreshableView().setDivider(null);
        mListDataBinding.list.getRefreshableView().setSelector(android.R.color.transparent);
        mListDataBinding.list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mILoadDataListView.getPresenter().onRefreshFromTop();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mILoadDataListView.getPresenter().onRefreshFromBottom();
            }
        });
        mListDataBinding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mILoadDataListView.onItemClick(parent, view, position, id);
            }
        });
        mAdapter = mILoadDataListView.getAdapter();
        mListDataBinding.list.setAdapter(mAdapter);
        ViewDataBinding viewDataBinding = getViewDataBinding();
        mListDataBinding.emptyView.addView(viewDataBinding.getRoot());
        mILoadDataListView.addListView(databinding,mListDataBinding.getRoot());
    }

    private ViewDataBinding getViewDataBinding() {
        return DataBindingViewGetter.Util.getViewDataBinding(mILoadDataListView.getEmptyViewGetter(), mILoadDataListView.getActivity());
    }

    protected LayoutListContainerBinding createListContainer() {
        return DataBindingUtil.inflate(LayoutInflater.from(mILoadDataListView.getActivity()), R.layout.layout_list_container, null, false);
    }

    public void setDatas(List<D> datas) {
        mAdapter.setDatas(datas);
    }

    public void addDatas(List<D> datas) {
        mAdapter.addDatas(datas);
    }

    public void setBottomRefreshEnable(boolean enable) {
        if (mILoadDataListView.getFreshMode() == PullToRefreshBase.Mode.BOTH) {
            if (enable) {
                mListDataBinding.list.setMode(PullToRefreshBase.Mode.BOTH);
            } else {
                mListDataBinding.list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }

    @Override
    public void hasData(boolean b) {

    }

    public void compliteRefresh() {
        mListDataBinding.list.onRefreshComplete();
    }

    public void setRefreshEnable(boolean enable) {
        mListDataBinding.list.setEnabled(enable);
    }

    public void showEmptyView() {
        mListDataBinding.emptyView.setVisibility(View.VISIBLE);
    }

    public void hideEmptyView() {
        mListDataBinding.emptyView.setVisibility(View.GONE);
    }

    @Override
    public PullToRefreshListView getListView() {
        return mListDataBinding.list;
    }

    public List<D> getCurrDatas() {
        return mAdapter.getDatas();
    }

    public void startRefresh() {
        mILoadDataListView.startRefresh();
    }

    public void finishRefresh() {
        mILoadDataListView.finishRefresh();
    }
}