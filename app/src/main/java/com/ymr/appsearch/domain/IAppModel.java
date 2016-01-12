package com.ymr.appsearch.domain;

import com.ymr.appsearch.bean.AppItem;
import com.ymr.common.IModel;
import com.ymr.common.net.NetWorkModel;
import com.ymr.common.net.SimpleResultNetWorkModel;

import java.util.List;

/**
 * Created by ymr on 15/12/19.
 */
public interface IAppModel extends IModel {
    void getAllAppItems(NetWorkModel.UpdateListener<List<AppItem>> updateListener);
    void updateAppItem(AppItem appItem, SimpleResultNetWorkModel.SimpleRequestListener listener);
    void deleteAppItem(AppItem appItem, SimpleResultNetWorkModel.SimpleRequestListener listener);
    void getAppItemsByNum(String numStr, NetWorkModel.UpdateListener<List<AppItem>> updateListener);

    void updateDatas(NetWorkModel.UpdateListener<List<AppItem>> updateListener);
}
