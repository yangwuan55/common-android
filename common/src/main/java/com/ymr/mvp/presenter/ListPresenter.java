package com.ymr.mvp.presenter;

import android.app.Activity;
import android.os.Handler;

import com.ymr.common.net.NetWorkModel;
import com.ymr.common.util.DeviceInfoUtils;
import com.ymr.mvp.model.IListDataModel;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.params.ListParams;
import com.ymr.mvp.view.IListView;

import java.util.List;

/**
 * Created by ymr on 15/8/20.
 */
public abstract class ListPresenter<D, E extends IListItemBean<D>> extends BaseNetPresenter {

    private static final int DEFAULT_START_PAGE = 1;
    private static final int PAGE_SIZE = 10;
    protected final IListView<D, E> mView;
    private int mPage;
    private IListDataModel<D, E> mModel;
    private int mStartPage = DEFAULT_START_PAGE;

    private Handler mHandler = new Handler();

    public ListPresenter(IListView<D, E> listView) {
        super(listView);
        mView = listView;
        mModel = createModel(mView.getActivity());
    }

    public void loadDatas() {
        if (mView.exist()) {
            if (DeviceInfoUtils.hasInternet(mView.getActivity())) {
                mView.hideNoNetWork();
                onRefreshFromTop();
            } else {
                mView.showNoNetWork();
                onHasNoInternet();
            }
            verifyInternet();
        }
    }

    protected void onHasNoInternet() {

    }

    @Override
    public void onNetDisconnect() {
        super.onNetDisconnect();
        if (mView.exist()) {
            mView.setRefreshEnable(false);
        }
    }

    @Override
    public void onNetConnect() {
        super.onNetConnect();
        if (mView.exist()) {
            mView.setRefreshEnable(true);
        }
    }

    private NetWorkModel.UpdateListener<E> mTopUpdateListener = new NetWorkModel.UpdateListener<E>() {
        @Override
        public void finishUpdate(E datas) {
            updateDatas(datas, true);
            mView.hideNoNetWork();
        }

        @Override
        public void onError(NetWorkModel.Error error) {
            loadError(error);
        }
    };

    private void loadError(NetWorkModel.Error error) {
        mView.onError(error.getMsg());
        mView.compliteRefresh();
    }

    private NetWorkModel.UpdateListener<E> mBottomUpdateListener = new NetWorkModel.UpdateListener<E>() {
        @Override
        public void finishUpdate(E datas) {
            updateDatas(datas, false);
        }

        @Override
        public void onError(NetWorkModel.Error error) {
            loadError(error);
        }
    };

    /**
     * @param wData
     * @param isTop            是否是从顶部刷新
     */
    private void updateDatas(E wData, boolean isTop) {
        if (wData != null) {
            receiveData(wData);
            List<D> datas = wData.getDatas();
            if (datas != null && !datas.isEmpty()) {
                mView.hasData(true);
                if (isTop) {
                    setDatas(datas);
                } else {
                    addDatas(datas);
                }
            } else {
                hasNoData();
                mView.setDatas(null);
                mView.hasData(false);
            }
            mView.compliteRefresh();
            if (wData.isLastpage()) {
                //mView.onMessage("is last page.");
                if (mView.exist()) {
                    mView.setBottomRefreshEnable(false);
                }
            }
        } else {
            mView.compliteRefresh();
            hasNoData();
            mView.hasData(false);
            mView.onError("no data error");
        }
        if (mView.isCurrView()) {
            mView.finishRefresh();
        }
    }

    protected void receiveData(E wData) {

    }
    protected void hasNoData() {
    }

    protected abstract IListDataModel<D, E> createModel(Activity activity);

    private void addDatas(List<D> datas) {
        mView.addDatas(datas);
        onAddDatas(datas);
    }

    protected void onAddDatas(List<D> studentlist) {

    }

    private void setDatas(List<D> datas) {
        mView.setDatas(datas);
        onSetDatas(datas);
    }

    protected void onSetDatas(List<D> studentlist) {

    }

    public void onRefreshFromBottom() {
        if (mView.exist()) {
            if (verifyInternet()) {
                if (mView.isCurrView()) {
                    mView.startRefresh();
                }
                mPage++;
                ListParams listParams = getListParams();
                listParams.setPageParam(mPage,PAGE_SIZE);
                mModel.updateListDatas(listParams, mBottomUpdateListener);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.compliteRefresh();
                    }
                });
            }
        }
    }

    protected abstract ListParams getListParams();

    public void onRefreshFromTop() {
        if (mView.exist()) {
            if (verifyInternet() && verifyFromChild()) {
                if (mView.isCurrView()) {
                    mView.startRefresh();
                }
                mPage = mStartPage;
                ListParams listParams = getListParams();
                listParams.setPageParam(mPage, PAGE_SIZE);
                mModel.updateListDatas(listParams, mTopUpdateListener);
                mView.setBottomRefreshEnable(true);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.compliteRefresh();
                    }
                });
            }
        }
    }

    protected boolean verifyFromChild() {
        return true;
    }

    public void setStartPage(int startPage) {
        mStartPage = startPage;
    }
}
