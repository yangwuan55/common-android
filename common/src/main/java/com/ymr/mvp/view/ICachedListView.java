package com.ymr.mvp.view;

import com.ymr.mvp.model.bean.IListItemBean;

/**
 * Created by ymr on 15/8/26.
 */
public interface ICachedListView<D, E extends IListItemBean<D>> extends IListView<D,E> {
    String getCachedName();
}
