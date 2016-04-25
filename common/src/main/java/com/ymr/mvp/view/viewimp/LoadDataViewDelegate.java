package com.ymr.mvp.view.viewimp;

import android.os.Handler;
import android.os.Message;

import com.ymr.common.Env;
import com.ymr.common.ui.view.SureDialog;
import com.ymr.common.util.ToastUtils;
import com.ymr.mvp.presenter.LoadDataPresenter;

/**
 * Created by ymr on 15/11/12.
 */
public class LoadDataViewDelegate<P extends LoadDataPresenter> {
    private static final int TIME_OUT = 5;
    private ILoadingAnimView<P> mLoadingAnimView;
    private long mTimeOut = 10000;
    public static final int SHOW_LOADING = 6;
    public static final int HIDE_LOADING = 7;
    private P mPresenter;
    private Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case TIME_OUT:
                    removeMessages(TIME_OUT);
                    if (mPresenter.getView().isCurrView()) {
                        mPresenter.getView().onMessage("网络超时");
                    }
                    hideLoading();
                    break;
                case SHOW_LOADING:
                    if (mPresenter.getView().isCurrView()) {
                        mLoadingAnimView.showLoadingView();
                    }
                    break;
                case HIDE_LOADING:
                    if (mPresenter.getView().exist()) {
                        mLoadingAnimView.hideLoadingView();
                    }
                    break;
            }
        }
    };

    LoadDataViewDelegate(P presenter) {
        mPresenter = presenter;
        if (Env.sLoadingAnimView == null) {
            mLoadingAnimView = new LoadingAnimView<>();
            mLoadingAnimView.onCreate(mPresenter);
        } else {
            setLoadingAnimView(Env.sLoadingAnimView);
        }
    }

    public void onError(final String msg) {
        onMessage(msg);
    }

    public void onMessage(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(mPresenter.getView().getActivity(), msg);
            }
        });
    }

    public void showLoading() {
        showLoading(0);
    }

    public void showLoading(long delay) {
        mHandler.sendEmptyMessageDelayed(SHOW_LOADING,delay);
        mHandler.removeMessages(TIME_OUT);
        if (mTimeOut != -1) {
            mHandler.sendEmptyMessageDelayed(TIME_OUT, mTimeOut);
        }
    }

    public void hideLoading() {
        mHandler.removeMessages(TIME_OUT);
        mHandler.sendEmptyMessageDelayed(HIDE_LOADING,800);
    }

    public P getPresenter() {
        return mPresenter;
    }

    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            if (mLoadingAnimView != null) {
                mLoadingAnimView.onDestroy();
            }
        }
    }

    public void showSureDialog(Runnable okRun, String content) {
        new SureDialog(getPresenter().getView().getActivity(), okRun)
                .setContent(content)
                .show();
    }

    public boolean backPressed() {
        return mPresenter.backPressed();
    }

    public void showSureDialog(Runnable okRun, String content, SureDialog.SureDialogListener listener) {
        new SureDialog(mPresenter.getView().getActivity(), okRun)
                .setContent(content)
                .setSureDialogListener(listener)
                .show();
    }

    public void setTimeOut(long timeOut) {
        mTimeOut = timeOut;
    }

    public void setLoadingAnimView(ILoadingAnimView<P> loadingAnimView) {
        this.mLoadingAnimView = loadingAnimView;
        mLoadingAnimView.onCreate(mPresenter);
    }
}
