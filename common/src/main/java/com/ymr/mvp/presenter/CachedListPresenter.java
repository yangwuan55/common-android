package com.ymr.mvp.presenter;

import android.app.Activity;

import com.ymr.mvp.model.ICachedListDataModel;
import com.ymr.mvp.model.IListDataModel;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.view.ICachedListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymr on 15/8/20.
 */
public abstract class CachedListPresenter<D, E extends IListItemBean<D>> extends ListPresenter<D,E> {

    private ICachedListDataModel<D, E> mModel;

    public CachedListPresenter(ICachedListView<D, E> listView) {
        super(listView);
        mModel.setCacheName(listView.getCachedName());
    }

    @Override
    protected IListDataModel<D, E> createModel(Activity activity) {
        mModel = createCachedModel(activity);
        return mModel;
    }

    protected abstract ICachedListDataModel<D,E> createCachedModel(Activity activity);

    @Override
    protected void onHasNoInternet() {
        updateLocalData();
    }

    private void updateLocalData() {
        List<D> cacheStudents = mModel.getCacheDatas();
        if (cacheStudents != null && !cacheStudents.isEmpty()) {
            mView.setDatas(cacheStudents);
            mView.hasData(true);
        } else {
            mView.hasData(false);
        }
    }

    @Override
    public void onRefreshFromTop() {
        updateLocalData();
        super.onRefreshFromTop();
    }

    @Override
    protected void onAddDatas(List<D> studentlist) {
        List<D> cacheStudents = mModel.getCacheDatas();
        cacheStudents.addAll(studentlist);
        mModel.cacheDatas(cacheStudents);
    }

    @Override
    protected void onSetDatas(List<D> studentlist) {
        mModel.cacheDatas(studentlist);
    }

    @Override
    protected void hasNoData() {
        super.hasNoData();
        mModel.cacheDatas(new ArrayList<D>());
    }
}
