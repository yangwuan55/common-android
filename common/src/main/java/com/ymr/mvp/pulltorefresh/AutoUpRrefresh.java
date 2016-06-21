package com.ymr.mvp.pulltorefresh;

import android.widget.AbsListView;

import com.ymr.mvp.presenter.ListPresenter;
import com.ymr.mvp.view.ILoadDataListView;

public class AutoUpRrefresh {

    private final ILoadDataListView mLoadDataListView;

    public AutoUpRrefresh(ILoadDataListView iLoadDataListView) {
        mLoadDataListView = iLoadDataListView;
    }

    public void setAutoUpRefresh() {
        mLoadDataListView.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    ListPresenter presenter = (ListPresenter) mLoadDataListView.getPresenter();
                    if (!presenter.isLastPage() && view.getLastVisiblePosition() >= view.getCount() - 2) {
                        presenter.onRefreshFromBottom();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }
}