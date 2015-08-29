package com.ymr.mvp.model;


import android.content.Context;

import com.ymr.common.SimpleModel;
import com.ymr.common.net.NetWorkModel;
import com.ymr.common.net.SimpleNetWorkModel;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.params.ListParams;

/**
 * Created by ymr on 15/8/20.
 */
public class ListDataModel<D, E extends IListItemBean<D>> extends SimpleModel implements IListDataModel<D,E> {

    private final SimpleNetWorkModel<E> mSimpleNetWorkModel;

    public ListDataModel(Context context, Class<E> eClass) {
        mSimpleNetWorkModel = new SimpleNetWorkModel<>(context, eClass);
    }

    @Override
    public void updateListDatas(ListParams netRequestParams, NetWorkModel.UpdateListener<E> updateListener) {
        mSimpleNetWorkModel.updateDatas(netRequestParams,updateListener);
    }


}
