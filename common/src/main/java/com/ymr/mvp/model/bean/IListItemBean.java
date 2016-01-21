package com.ymr.mvp.model.bean;

import java.util.List;

/**
 * Created by ymr on 15/8/20.
 */
public interface IListItemBean<T> {

    List<T> getDatas();

    void setDatas(List<T> datas);

    boolean isLastpage();

    void setLastPage(boolean isLast);
}
