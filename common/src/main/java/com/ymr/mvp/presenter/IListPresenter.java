package com.ymr.mvp.presenter;

import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.view.IListView;
import com.ymr.mvp.view.ILoadDataView;

/**
 * Created by ymr on 15/10/20.
 */
public interface IListPresenter<D, E extends IListItemBean<D>,V extends IListView<D,E> & ILoadDataView> extends IBasePresenter<V>{
    void setPageSize(int pageSize);

    void loadDatas();

    void receiveData(E wData,boolean isTop);

    void onRefreshFromBottom();

    void refreshCurrItem(int position);

    void onRefreshFromTop();

    boolean isLastPage();
}
