package com.ymr.mvp.presenter;

import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.view.IListViewHasEmptyView;

/**
 * Created by ymr on 15/10/20.
 */
public abstract class ListPresenterHasEmpty<D, E extends IListItemBean<D>,V extends IListViewHasEmptyView<D,E>> extends ListPresenter<D,E,V> {

    public ListPresenterHasEmpty(V listView) {
        super(listView);
    }

    @Override
    public void receiveData(E wData) {
        super.receiveData(wData);
        if (wData == null || wData.getDatas() == null || wData.getDatas().isEmpty()) {
            getView().showEmptyView();
        } else {
            getView().hideEmptyView();
        }
    }
}
