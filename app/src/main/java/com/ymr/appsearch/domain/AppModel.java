package com.ymr.appsearch.domain;

import android.content.Context;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.ymr.appsearch.bean.AppItem;
import com.ymr.appsearch.db.DBHelper;
import com.ymr.appsearch.t9.T9Search;
import com.ymr.common.SimpleModel;
import com.ymr.common.net.NetWorkModel;
import com.ymr.common.net.SimpleResultNetWorkModel;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by ymr on 15/12/19.
 */
public class AppModel extends SimpleModel implements IAppModel {

    private final Context mContext;
    private final IAppGetter mAppGetter;
    private final T9Search<AppItem> mT9Search;
    private Dao<AppItem, ?> mDao;

    public AppModel(Context context) {
        mContext = context;
        mT9Search = new T9Search<>();
        mAppGetter = new DBAppGetter(mContext);
        try {
            mDao = DBHelper.getInstance(mContext).getDao(AppItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllAppItems(final NetWorkModel.UpdateListener<List<AppItem>> updateListener) {
        mAppGetter.getApps(new NetWorkModel.UpdateListener<List<AppItem>>() {
            @Override
            public void finishUpdate(List<AppItem> result) {
                Collections.sort(result);
                mT9Search.setAllItems(result);
                updateListener.finishUpdate(result);
            }

            @Override
            public void onError(NetWorkModel.Error error) {
                updateListener.onError(error);
            }
        });
    }

    @Override
    public void updateAppItem(AppItem appItem, SimpleResultNetWorkModel.SimpleRequestListener listener) {
        try {
            mDao.update(appItem);
            listener.onSuccess();
            notifyListeners();
        } catch (SQLException e) {
            NetWorkModel.Error error = new NetWorkModel.Error();
            error.setMsg(e.toString());
            listener.onFail(error);
        }
    }

    @Override
    public void deleteAppItem(AppItem appItem, SimpleResultNetWorkModel.SimpleRequestListener listener) {
        try {
            mDao.delete(appItem);
            listener.onSuccess();
        } catch (SQLException e) {
            NetWorkModel.Error error = new NetWorkModel.Error();
            error.setMsg(e.toString());
            listener.onFail(error);
        }
    }

    @Override
    public void getAppItemsByNum(String str, NetWorkModel.UpdateListener<List<AppItem>> updateListener) {
        if (TextUtils.isEmpty(str)) {
            getAllAppItems(updateListener);
        } else {
            List<AppItem> search = mT9Search.search(str);
            updateListener.finishUpdate(search);
        }
    }

    @Override
    public void updateDatas(NetWorkModel.UpdateListener<List<AppItem>> updateListener) {
        mAppGetter.updateDatas(updateListener);
    }
}
