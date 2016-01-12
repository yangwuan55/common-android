package com.ymr.appsearch.domain;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ymr.appsearch.Utils;
import com.ymr.appsearch.bean.AppItem;
import com.ymr.common.net.NetWorkModel;
import com.ymr.common.util.LOGGER;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ymr on 15/11/21.
 */
public abstract class AbsAppGetter implements IAppGetter {

    private static final String TAG = "AbsAppGetter";
    private final Context mContext;
    private final PackageManager mPackageManager;
    private DataManager mDataManager;

    public AbsAppGetter(Context context) {
        mContext = context;
        mPackageManager = getContext().getPackageManager();
        onCreate();
        mDataManager = new DataManager();
    }

    protected abstract void onCreate();

    @Override
    public void getApps(final NetWorkModel.UpdateListener<List<AppItem>> appListener) {
        if (hasCache()) {
            doResult(appListener);
        } else {
            new AsyncTask<Void, Void, List<AppItem>>() {

                private long time;

                @Override
                protected void onPreExecute() {
                    time = System.currentTimeMillis();
                }

                @Override
                protected List<AppItem> doInBackground(Void... params) {
                    return loadApps();
                }

                @Override
                protected void onPostExecute(List<AppItem> appItems) {
                    cacheData(appItems);
                    doResult(appListener);
                    Log.i(TAG,"TIME = " + (System.currentTimeMillis() - time));
                }
            }.execute();
        }
    }

    @Override
    public void updateDatas(final NetWorkModel.UpdateListener<List<AppItem>> appListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mDataManager.update();
                if (hasCache()) {
                    checkCurrApps();
                    checkSavedApps();
                }
                doResult(appListener);
                return null;
            }
        }.execute();
    }

    private void checkSavedApps() {
        List<AppItem> cache = mDataManager.getCache();
        List<ApplicationInfo> allAppInfos = mDataManager.getAllAppInfos();
        for (AppItem next : cache) {
            boolean currHas = false;
            for (ApplicationInfo applicationInfo : allAppInfos) {
                if (applicationInfo.packageName.equals(next.getPackageName())) {
                    currHas = true;
                    break;
                }
            }

            if (!currHas) {
                deleteApp(next);
                LOGGER.i(TAG,"delete = " + next);
            }
        }
    }

    private void checkCurrApps() {
        List<ApplicationInfo> allAppInfos = mDataManager.getAllAppInfos();
        List<AppItem> cache = mDataManager.getCache();
        for (ApplicationInfo next : allAppInfos) {
            boolean hasSave = false;
            for (AppItem appItem : cache) {
                if (appItem.getPackageName().equals(next.packageName)) {
                    hasSave = true;
                }
            }

            if (!hasSave) {
                try {
                    AppItem appItem = getAppItem(next);
                    saveAppItem(appItem);
                    LOGGER.i(TAG,"add = " + appItem);
                } catch (SQLException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected abstract void deleteApp(AppItem appItem);

    protected abstract void saveAppItem(AppItem appItem);

    private void doResult(NetWorkModel.UpdateListener<List<AppItem>> appListener) {
        appListener.finishUpdate(getCache());
    }

    protected abstract List<AppItem> getCache();

    protected abstract void cacheData(List<AppItem> data);

    protected List<AppItem> loadApps() {
        List<AppItem> result = new ArrayList<>();
        Iterator<ApplicationInfo> infoIterator = getAllAppInfos().iterator();
        try {
            while (infoIterator.hasNext()) {
                ApplicationInfo next = infoIterator.next();
                AppItem appitem = getAppItem(next);
                result.add(appitem);
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @NonNull
    private AppItem getAppItem(ApplicationInfo next) throws SQLException, FileNotFoundException {
        AppItem appitem = new AppItem();

        appitem.setName(next.loadLabel(mPackageManager).toString());
        appitem.setPinyin(Utils.getPinyinNum(appitem.getName().replace(" ", "")));
        appitem.setFullpinyin(Utils.getPinyinNum(appitem.getName().replace(" ", ""), true));
        appitem.setPackageName(next.packageName);

        Drawable drawable = next.loadIcon(mPackageManager);
        Bitmap bitmap = Utils.drawableToBitmap(drawable);
        String path = Utils.generateFilePath(getContext(), appitem.getPackageName());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(path));
        appitem.setImage(path);
        return appitem;
    }

    @NonNull
    private List<ApplicationInfo> getAllAppInfos() {
        List<ApplicationInfo> installedApplications = mPackageManager.getInstalledApplications(0);
        Iterator<ApplicationInfo> infoIterator = installedApplications.iterator();
        while (infoIterator.hasNext()) {
            ApplicationInfo next = infoIterator.next();
            if (mPackageManager.getLaunchIntentForPackage(next.packageName) == null) {
                infoIterator.remove();
            }
        }
        return installedApplications;
    }

    private boolean hasCache() {
        List<AppItem> cache = getCache();
        return cache != null && !cache.isEmpty();
    }

    public Context getContext() {
        return mContext;
    }

    private class DataManager {
        private List<AppItem> cache;
        private List<ApplicationInfo> allAppInfos;

        public List<AppItem> getCache() {
            return cache;
        }

        public List<ApplicationInfo> getAllAppInfos() {
            return allAppInfos;
        }

        public void update() {
            cache = AbsAppGetter.this.getCache();
            allAppInfos = AbsAppGetter.this.getAllAppInfos();
        }
    }
}
