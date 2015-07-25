package com.ymr.common.ui;

import com.ymr.common.net.NetWorkController;

/**
 * Created by ymr on 15/7/7.
 */
public interface NetWorkUI<D,P> extends BaseUI {

    int getLoadFailViewId();

    int getNetErorrViewId();

    NetWorkController<D,P> getNetWorkController();

}
