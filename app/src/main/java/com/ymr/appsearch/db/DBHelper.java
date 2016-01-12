package com.ymr.appsearch.db;

import android.content.Context;

import com.ymr.appsearch.bean.AppItem;
import com.ymr.dao.AbsBean;
import com.ymr.dao.DaoHelper;
import com.ymr.dao.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymr on 15/11/21.
 */
public class DBHelper extends DaoHelper {
    public static final String DATE_BASE_NAME = "app_db";
    public static final int DATEBSE_VERSION = 2;
    private static DBHelper sInstance;

    private DBHelper(Context context) {
        super(context, DATE_BASE_NAME, 4);
    }

    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    protected DataSource getDataSource() {
        return new DataSource() {
            @Override
            public List<Class<? extends AbsBean>> getDataClasses() {
                ArrayList<Class<? extends AbsBean>> classes = new ArrayList<>();
                classes.add(AppItem.class);
                return classes;
            }
        };
    }
}
