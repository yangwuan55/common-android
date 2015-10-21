package com.ymr.mvp.presenter;

import android.app.Activity;
import android.os.Handler;

import com.ymr.common.net.NetWorkModel;
import com.ymr.common.util.DeviceInfoUtils;
import com.ymr.mvp.model.IListDataModel;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.params.ListParams;
import com.ymr.mvp.view.IListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymr on 15/8/20.
 */
public abstract class ListPresenter<D, E extends IListItemBean<D>,V extends IListView<D,E>> extends BaseNetPresenter<V> implements IListPresenter<D,E,V> {

    private static final int DEFAULT_START_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private int mPage;
    private IListDataModel<D, E> mModel;
    private int mStartPage = DEFAULT_START_PAGE;
    private int mPageSize = DEFAULT_PAGE_SIZE;

    private Handler mHandler = new Handler();

    public ListPresenter(V listView) {
        super(listView);
        mModel = createModel(getView().getActivity());
    }

    @Override
    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }

    @Override
    public void loadDatas() {
        if (getView().exist()) {
            if (DeviceInfoUtils.hasInternet(getView().getActivity())) {
                getView().hideNoNetWork();
                onRefreshFromTop();
            } else {
                getView().showNoNetWork();
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
        if (getView().exist()) {
            getView().setRefreshEnable(false);
        }
    }

    @Override
    public void onNetConnect() {
        super.onNetConnect();
        if (getView().exist()) {
            getView().setRefreshEnable(true);
        }
    }

    private NetWorkModel.UpdateListener<E> mTopUpdateListener = new NetWorkModel.UpdateListener<E>() {
        @Override
        public void finishUpdate(E datas) {
            updateDatas(datas, true);
            getView().hideNoNetWork();
        }

        @Override
        public void onError(NetWorkModel.Error error) {
            loadError(error);
        }
    };

    private void loadError(NetWorkModel.Error error) {
        getView().onError(error.getMsg());
        getView().compliteRefresh();
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

    private class RefreshCurrListener implements NetWorkModel.UpdateListener<E> {

        private final int mPosition;

        public RefreshCurrListener(int position) {
            mPosition = position;
        }


        @Override
        public void finishUpdate(E datas) {
            replaceDatas(datas,mPosition);
        }

        @Override
        public void onError(NetWorkModel.Error error) {
            loadError(error);
        }
    }

    private void replaceDatas(E wDatas, int position) {
        if (wDatas != null) {
            List<D> newDatas = wDatas.getDatas();
            List<D> currDatas = getView().getCurrDatas();
            int pageByPosition = getPageByPosition(position);
            int start = mPageSize * (pageByPosition - mStartPage);
            int end = mPageSize * (pageByPosition - mStartPage + 2) - 1;
            List<D> oldList = currDatas.subList(start, end > currDatas.size() - 1 ? currDatas.size() - 1 : end);
            deleteForChange(currDatas, newDatas, oldList);

            getView().setDatas(currDatas);
        }
    }

    private void deleteForChange(List<D> currDatas, List<D> newDatas, List<D> oldList) {
        List<D> listForDelete = new ArrayList<>();
        for (int i = 0; i < oldList.size(); i++) {
            D data = oldList.get(i);
            if (!newDatas.contains(data)) {
                listForDelete.add(data);
            }
        }

        currDatas.removeAll(listForDelete);
    }

    /**
     * @param wData
     * @param isTop            是否是从顶部刷新
     */
    private void updateDatas(E wData, boolean isTop) {
        if (wData != null) {
            receiveData(wData);
            List<D> datas = wData.getDatas();
            if (datas != null && !datas.isEmpty()) {
                getView().hasData(true);
                if (isTop) {
                    setDatas(datas);
                } else {
                    addDatas(datas);
                }
            } else {
                hasNoData();
                getView().setDatas(null);
                getView().hasData(false);
            }
            getView().compliteRefresh();
            if (wData.isLastpage()) {
                //getView().onMessage("is last page.");
                if (getView().exist()) {
                    getView().setBottomRefreshEnable(false);
                }
            }
        } else {
            getView().compliteRefresh();
            hasNoData();
            getView().hasData(false);
            getView().onError("no data error");
        }
        if (getView().isCurrView()) {
            getView().finishRefresh();
        }
    }

    @Override
    public void receiveData(E wData) {

    }
    protected void hasNoData() {
    }

    protected abstract IListDataModel<D, E> createModel(Activity activity);

    private void addDatas(List<D> datas) {
        getView().addDatas(datas);
        onAddDatas(datas);
    }

    protected void onAddDatas(List<D> studentlist) {

    }

    private void setDatas(List<D> datas) {
        getView().setDatas(datas);
        onSetDatas(datas);
    }

    protected void onSetDatas(List<D> studentlist) {

    }

    @Override
    public void onRefreshFromBottom() {
        if (getView().exist()) {
            if (verifyInternet()) {
                notifyStartRefresh();
                mPage++;
                doUpdate(mBottomUpdateListener);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().compliteRefresh();
                    }
                });
            }
        }
    }

    @Override
    public void refreshCurrItem(int position) {
        if (getView().exist()) {
            if (verifyInternet()) {
                notifyStartRefresh();
                doUpdate(new RefreshCurrListener(position),getPageByPosition(position),mPageSize);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().compliteRefresh();
                    }
                });
            }
        }
    }

    private int getPageByPosition(int position) {
        return position/mPageSize + mStartPage;
    }

    private void notifyStartRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getView().isCurrView()) {
                    getView().startRefresh();
                }
            }
        },100);
    }

    private void doUpdate(NetWorkModel.UpdateListener<E> mBottomUpdateListener) {
        doUpdate(mBottomUpdateListener,mPage,mPageSize);
    }

    private void doUpdate(NetWorkModel.UpdateListener<E> mBottomUpdateListener,int page,int pagesize) {
        ListParams listParams = getListParams();
        listParams.setPageParam(page, pagesize);
        mModel.updateListDatas(listParams, mBottomUpdateListener);
    }

    protected abstract ListParams getListParams();

    @Override
    public void onRefreshFromTop() {
        if (getView().exist()) {
            if (verifyInternet() && verifyFromChild()) {
                notifyStartRefresh();
                mPage = mStartPage;
                doUpdate(mTopUpdateListener);
                getView().setBottomRefreshEnable(true);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().compliteRefresh();
                    }
                });
            }
        }
    }

    protected boolean verifyFromChild() {
        return true;
    }

    @Override
    public void setStartPage(int startPage) {
        mStartPage = startPage;
    }
}
