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
        if (isEmpty(wData)) {
            getView().showEmptyView();
        } else {
            getView().hideEmptyView();
        }
    }

    protected boolean isEmpty(E wData) {
        return wData == null || wData.getDatas() == null || wData.getDatas().isEmpty();
    }
}
