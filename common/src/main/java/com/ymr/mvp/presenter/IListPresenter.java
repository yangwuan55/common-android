package com.ymr.mvp.presenter;

import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.view.IListView;

/**
 * Created by ymr on 15/10/20.
 */
public interface IListPresenter<D, E extends IListItemBean<D>,V extends IListView<D,E>> extends IBasePresenter<V>{
    void setPageSize(int pageSize);

    void loadDatas();

    void receiveData(E wData);

    void onRefreshFromBottom();

    void onRefreshFromTop();

    void setStartPage(int startPage);
}
