package com.ymr.mvp.pulltorefresh;

import android.graphics.ImageFormat;
import android.widget.AbsListView;

import com.ymr.mvp.presenter.ListPresenter;
import com.ymr.mvp.view.ILoadDataListView;

public class AutoUpRrefresh {

    private final IAutoUpRefreshView mLoadDataListView;

    public AutoUpRrefresh(IAutoUpRefreshView iLoadDataListView) {
        mLoadDataListView = iLoadDataListView;
    }

    public void setAutoUpRefresh() {
        mLoadDataListView.getListView().setOnScrollListener(new OnScrollListenerWrapper(mLoadDataListView.createOnScrollListener()));
    }

    class OnScrollListenerWrapper implements AbsListView.OnScrollListener {

        private final AbsListView.OnScrollListener mListener;

        public OnScrollListenerWrapper(AbsListView.OnScrollListener listener) {
            mListener = listener;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                ListPresenter presenter = (ListPresenter) mLoadDataListView.getPresenter();
                if (!presenter.isLastPage() && view.getLastVisiblePosition() >= view.getCount() - 2) {
                    presenter.onRefreshFromBottom();
                }
            }
            if (mListener != null) {
                mListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mListener != null) {
                mListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    }
}