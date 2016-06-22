package com.ymr.mvp.view;

import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.ymr.common.ui.adapter.DataBaseAdapter;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.presenter.ListPresenterHasEmpty;

/**
 * Created by ymr on 16/4/11.
 */
public interface ILoadDataListView<D, E extends IListItemBean<D>,P extends ListPresenterHasEmpty<D,E,? extends IListViewHasEmptyView<D,E>>> extends ILoadDataView<P>,IListViewHasEmptyView<D,E> {
    Drawable getBackGround();

    void onItemClick(AdapterView<?> parent, View view, int position, long id);

    DataBaseAdapter<D> getAdapter();

    DataBindingViewGetter getEmptyViewGetter();

    void addListView(ViewDataBinding databinding, View listView);

    PullToRefreshBase.Mode getFreshMode();
}
