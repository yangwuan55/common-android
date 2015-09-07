package com.ymr.mvp.view;


import com.ymr.mvp.model.bean.IListItemBean;

import java.util.List;

/**
 * Created by ymr on 15/8/20.
 */
public interface IListView<D, E extends IListItemBean<D>> extends INetView,IActivityView{

    Class<E> getListItemClass();

    void setDatas(List<D> datas);

    void addDatas(List<D> datas);

    void setBottomRefreshEnable(boolean enable);

    void hasData(boolean b);

    void compliteRefresh();

    void setRefreshEnable(boolean enable);

    void startRefresh();

    void finishRefresh();
}
