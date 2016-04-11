package com.ymr.mvp.view.viewimp;

import android.databinding.ViewDataBinding;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.presenter.ListPresenterHasEmpty;
import com.ymr.mvp.view.IListViewHasEmptyView;
import com.ymr.mvp.view.ILoadDataListView;

import java.util.List;

/**
 * Created by ymr on 16/4/11.
 */
public abstract class MvpListViewActivity<D, E extends IListItemBean<D>,P extends ListPresenterHasEmpty<D,E,? extends IListViewHasEmptyView<D,E>>> extends LoadDataActivityView<P> implements ILoadDataListView<D,E,P> {
    private final ListViewDelegate<D,E,P> mListViewDelegate = new ListViewDelegate<>(this);

    @Override
    protected void finishCreatePresenter(ViewDataBinding databinding) {
        mListViewDelegate.finishCreatePresenter(databinding);
    }

    @Override
    public void setDatas(List<D> datas) {
        mListViewDelegate.setDatas(datas);
    }

    @Override
    public void addDatas(List<D> datas) {
        mListViewDelegate.addDatas(datas);
    }

    @Override
    public void setBottomRefreshEnable(boolean enable) {
        mListViewDelegate.setBottomRefreshEnable(enable);
    }

    @Override
    public void compliteRefresh() {
        mListViewDelegate.compliteRefresh();
    }

    @Override
    public void setRefreshEnable(boolean enable) {
        mListViewDelegate.setRefreshEnable(enable);
    }

    @Override
    public void showEmptyView() {
        mListViewDelegate.showEmptyView();
    }

    @Override
    public void hideEmptyView() {
        mListViewDelegate.hideEmptyView();
    }

    @Override
    public PullToRefreshListView getListView() {
        return mListViewDelegate.getListView();
    }

    @Override
    public List<D> getCurrDatas() {
        return mListViewDelegate.getCurrDatas();
    }
}
