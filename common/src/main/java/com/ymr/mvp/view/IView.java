package com.ymr.mvp.view;

/**
 * Created by ymr on 15/8/13.
 */
public interface IView extends IActivityView {

    void onError(String msg);

    void onMessage(String msg);

    boolean isCurrView();

    boolean exist();
}
