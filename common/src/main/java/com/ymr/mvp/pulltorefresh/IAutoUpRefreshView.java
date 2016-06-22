package com.ymr.mvp.pulltorefresh;

import android.widget.AbsListView;

import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.presenter.ListPresenterHasEmpty;
import com.ymr.mvp.view.IListViewHasEmptyView;
import com.ymr.mvp.view.ILoadDataListView;

/**
 * Created by ymr on 16/6/21.
 */
public interface IAutoUpRefreshView<D, E extends IListItemBean<D>,P extends ListPresenterHasEmpty<D,E,? extends IListViewHasEmptyView<D,E>>> extends ILoadDataListView<D,E,P> {
    AbsListView.OnScrollListener createOnScrollListener();
}
