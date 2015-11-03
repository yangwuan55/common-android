package com.ymr.mvp.view;

import com.ymr.mvp.model.bean.IListItemBean;

/**
 * Created by ymr on 15/10/20.
 */
public interface IListViewHasEmptyView<D, E extends IListItemBean<D>> extends IListView<D,E> {
    void showEmptyView();
    void hideEmptyView();
}
