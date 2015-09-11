package com.ymr.mvp.model;


import com.ymr.mvp.model.bean.ICachedListModel;
import com.ymr.mvp.model.bean.IListItemBean;

/**
 * Created by ymr on 15/8/20.
 */
public interface ICachedListDataModel<D, E extends IListItemBean<D>> extends IListDataModel<D,E>,ICachedListModel<D> {
    void setCacheName(String cacheName);
}
