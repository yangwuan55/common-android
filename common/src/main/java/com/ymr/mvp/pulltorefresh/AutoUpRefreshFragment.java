package com.ymr.mvp.pulltorefresh;

import android.databinding.ViewDataBinding;
import android.widget.AbsListView;

import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.presenter.ListPresenterHasEmpty;
import com.ymr.mvp.view.IListViewHasEmptyView;
import com.ymr.mvp.view.viewimp.MvpListViewFragment;

/**
 * Created by ymr on 16/5/5.
 */
public abstract class AutoUpRefreshFragment<D, E extends IListItemBean<D>,P extends ListPresenterHasEmpty<D,E,? extends IListViewHasEmptyView<D,E>>> extends MvpListViewFragment<D,E,P> implements IAutoUpRefreshView<D,E,P> {
    private final AutoUpRrefresh mAutoUpRrefresh = new AutoUpRrefresh(this);
    @Override
    protected void finishCreatePresenter(ViewDataBinding databinding) {
        super.finishCreatePresenter(databinding);
        mAutoUpRrefresh.setAutoUpRefresh();
    }

    @Override
    public AbsListView.OnScrollListener createOnScrollListener() {
        return null;
    }
}
