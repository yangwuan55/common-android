package com.ymr.mvp.model;


import com.ymr.common.IModel;
import com.ymr.common.net.NetWorkModel;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.params.ListParams;

/**
 * Created by ymr on 15/8/20.
 */
public interface IListDataModel<D, E extends IListItemBean<D>> extends IModel {
    void updateListDatas(ListParams netRequestParams, NetWorkModel.UpdateListener<E> updateListener);
}
