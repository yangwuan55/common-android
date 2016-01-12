package com.ymr.appsearch.domain;

import com.ymr.appsearch.bean.AppItem;
import com.ymr.common.net.NetWorkModel;

import java.util.List;

/**
 * Created by ymr on 15/11/21.
 */
public interface IAppGetter {
    void getApps(NetWorkModel.UpdateListener<List<AppItem>> appListener);

    void updateDatas(NetWorkModel.UpdateListener<List<AppItem>> appListener);
}
