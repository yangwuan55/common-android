package com.ymr.mvp.view.viewimp;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.ymr.common.ui.view.SureDialog;
import com.ymr.common.util.ToastUtils;
import com.ymr.mvp.presenter.LoadDataPresenter;
import com.ymr.mvp.view.IView;

/**
 * Created by ymr on 15/11/12.
 */
public class LoadDataActivityViewDelegate<P extends LoadDataPresenter> {
    private static final int TIME_OUT = 5;
    private long mTimeOut = 10000;
    public static final int SHOW_LOADING = 6;
    public static final int HIDE_LOADING = 7;
    private P mPresenter;
    private ProgressDialog mProgressDialog;
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
                        //mPresenter.getView().getRootView().setVisibility(View.INVISIBLE);
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setMessage("正在加载...");
                        if (mPresenter.getView().isCurrView()) {
                            mProgressDialog.show();
                        }
                    }
                    break;
                case HIDE_LOADING:
                    if (mPresenter.getView().exist()) {
                        IView view = mPresenter.getView();
                        if (view.isCurrView()) {
                            //view.getRootView().setVisibility(View.VISIBLE);
                            mProgressDialog.dismiss();
                        }
                    }
                    break;
            }
        }
    };

    LoadDataActivityViewDelegate(P presenter) {
        mPresenter = presenter;
        mProgressDialog = new ProgressDialog(mPresenter.getView().getActivity());
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
}
