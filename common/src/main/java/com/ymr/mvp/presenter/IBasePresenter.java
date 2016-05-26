package com.ymr.mvp.presenter;

import com.ymr.mvp.view.IView;

/**
 * Created by ymr on 15/10/20.
 */
public interface IBasePresenter<V extends IView> {
    V getView();

    boolean isViewExist();
}
