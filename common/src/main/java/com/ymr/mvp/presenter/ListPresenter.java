package com.ymr.mvp.presenter;

import android.app.Activity;
import android.os.Handler;

import com.ymr.common.net.NetWorkModel;
import com.ymr.mvp.model.IListDataModel;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.params.ListParams;
import com.ymr.mvp.view.IListView;
import com.ymr.mvp.view.ILoadDataView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymr on 15/8/20.
 */
public abstract class ListPresenter<D, E extends IListItemBean<D>,V extends IListView<D,E> & ILoadDataView> extends LoadDataPresenter<V> implements IListPresenter<D,E,V> {

    public static final int DEFUALT_STARTPAGE = 1;
    public static final int DEFUALT_PAGESIZE = 10;
    private int mPage;
    private IListDataModel<D, E> mModel;
    private int mStartPage = DEFUALT_STARTPAGE;
    private int mPageSize = DEFUALT_PAGESIZE;

    private Handler mHandler = new Handler();
    private boolean isLoading;
    private boolean hasInitParams = false;
    private boolean isLast;

    public ListPresenter(V listView) {
        super(listView);
        mModel = createModel(getView().getActivity());
        ListParams listParams = getListParams();
        initParams(listParams);
    }

    private void initParams(ListParams listParams) {
        if (listParams != null && !hasInitParams) {
            hasInitParams = true;
            mStartPage = listParams.getStartPage();
            mPageSize = listParams.getPagesize();
        }
    }

    @Override
    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }

    @Override
    public void loadDatas() {
        if (getView().exist()) {
            if (verify()) {
                getView().hideNoNetWork();
                onRefreshFromTop();
            } else {
                getView().showNoNetWork();
                onHasNoInternet();
            }
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
            int end = mPageSize * (pageByPosition - mStartPage + 2);
            List<D> oldList = null;
            int setEnd = end > currDatas.size() ? currDatas.size() : end;
            oldList = currDatas.subList(start, setEnd);
            deleteForChange(currDatas, newDatas, oldList);

            getView().setDatas(currDatas);
            if (currDatas.isEmpty()) {
                getView().hasData(false);
            } else {
                getView().hasData(true);
            }
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
            receiveData(wData,isTop);
            List<D> datas = wData.getDatas();
            if (isTop) {
                if (datas != null && !datas.isEmpty()) {
                    getView().hasData(true);
                    setDatas(datas);
                } else {
                    hasNoData();
                    getView().setDatas(null);
                    getView().hasData(false);
                }
            } else {
                addDatas(datas);
            }
            if (wData.isLastpage()) {
                if (getView().exist()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            isLast = true;
                            getView().setBottomRefreshEnable(false);
                        }
                    });
                }
            }
        } else if (isTop){
            hasNoData();
            getView().hasData(false);
            getView().onError("no data error");
        } else {
            addDatas(null);
        }
        getView().compliteRefresh();
        if (getView().isCurrView()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getView().finishRefresh();
                }
            },200);
        }
        isLoading = false;
    }

    @Override
    public void receiveData(E wData,boolean isTop) {

    }
    protected void hasNoData() {
    }

    protected abstract IListDataModel<D, E> createModel(Activity activity);

    private void addDatas(List<D> datas) {
        if (datas != null && !datas.isEmpty()) {
            getView().addDatas(datas);
            onAddDatas(datas);
        } else {
            addNullDatas();
        }
    }

    protected void addNullDatas() {

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
        if (getView().exist() && !isLoading() && !isLast) {
            if (verify()) {
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

    private boolean isLoading() {
        return isLoading;
    }

    @Override
    public void refreshCurrItem(int position) {
        if (getView().exist()) {
            if (verify()) {
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

    private void doUpdate(NetWorkModel.UpdateListener<E> updateListener) {
        isLoading = true;
        doUpdate(updateListener,mPage,mPageSize);
    }

    private void doUpdate(NetWorkModel.UpdateListener<E> updateListener,int page,int pagesize) {
        ListParams listParams = getListParams();
        if (listParams != null) {
            initParams(listParams);
            listParams.setCurrPage(page);
            if (getView().getCurrDatas().isEmpty()) {
                mModel.updateListDatas(listParams, wrapNetListener(updateListener));
            } else {
                mModel.updateListDatas(listParams, updateListener);
            }
        }
    }

    protected abstract ListParams getListParams();

    @Override
    public void onRefreshFromTop() {
        if (getView().exist() && !isLoading()) {
            if (verify()) {
                notifyStartRefresh();
                mPage = mStartPage;
                isLast = false;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        doUpdate(mTopUpdateListener);
                    }
                });
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

    @Override
    public boolean isLastPage() {
        return isLast;
    }

    protected boolean verify() {
        return verifyInternet() && verifyFromChild();
    }

    protected boolean verifyFromChild() {
        return true;
    }
}
