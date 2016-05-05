package com.ymr.mvp.pulltorefresh;

import android.databinding.ViewDataBinding;

import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.presenter.ListPresenterHasEmpty;
import com.ymr.mvp.view.IListViewHasEmptyView;
import com.ymr.mvp.view.viewimp.MvpListViewActivity;

/**
 * Created by ymr on 16/5/4.
 */
public abstract class AutoUpRefreshActivity<D, E extends IListItemBean<D>,P extends ListPresenterHasEmpty<D,E,? extends IListViewHasEmptyView<D,E>>> extends MvpListViewActivity<D,E,P> {
    private final AutoUpRrefresh autoUpRrefresh = new AutoUpRrefresh(this);
    @Override
    protected void finishCreatePresenter(ViewDataBinding databinding) {
        super.finishCreatePresenter(databinding);
        autoUpRrefresh.setAutoUpRefresh();
    }
}
