package com.ymr.mvp.view.viewimp;

import android.app.ProgressDialog;

import com.ymr.mvp.presenter.LoadDataPresenter;
import com.ymr.mvp.view.IView;

public class LoadingAnimView<P extends LoadDataPresenter> implements ILoadingAnimView<P> {
    private P mPresenter;
    ProgressDialog mProgressDialog;

    @Override
    public void hideLoadingView() {
        IView view = mPresenter.getView();
        if (view.isCurrView()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showLoadingView() {
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("正在加载...");
        if (mPresenter.getView().isCurrView()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void onCreate(P presenter) {
        mPresenter = presenter;
        mProgressDialog = new ProgressDialog(mPresenter.getView().getActivity());
    }

    @Override
    public void onDestroy() {
        mPresenter = null;
        mProgressDialog = null;
    }
}