package com.ymr.appsearch.domain;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.ymr.appsearch.bean.AppItem;
import com.ymr.appsearch.db.DBHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by ymr on 15/11/21.
 */
public class DBAppGetter extends AbsAppGetter {

    private static final String TAG = "DBAppGetter";
    private Dao<AppItem, ?> mDao;

    public DBAppGetter(Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        try {
            mDao = DBHelper.getInstance(getContext()).getDao(AppItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void deleteApp(AppItem appItem) {
        try {
            appItem.setState(AppItem.STATE_DISABLE);
            mDao.update(appItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void saveAppItem(AppItem appItem) {
        try {
            List<AppItem> appItems = mDao.queryForEq(AppItem.COL_PACKAGE_NAME, appItem.getPackageName());
            if (appItems != null && !appItems.isEmpty()) {
                appItems.get(0).setState(AppItem.STATE_ENABLE);
                mDao.update(appItems.get(0));
            } else {
                mDao.create(appItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<AppItem> getCache() {
        try {
            return mDao.queryForEq(AppItem.COL_STATE,AppItem.STATE_ENABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void cacheData(final List<AppItem> data) {
        try {
            mDao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (AppItem appItem : data) {
                        mDao.create(appItem);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
