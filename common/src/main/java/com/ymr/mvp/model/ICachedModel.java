package com.ymr.mvp.model;

import java.util.List;

/**
 * Created by ymr on 15/8/20.
 */
public interface ICachedModel<D> {
    void cacheDatas(D data);

    D getCacheDatas();
}
