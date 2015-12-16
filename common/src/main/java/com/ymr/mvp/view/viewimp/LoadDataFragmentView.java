package com.ymr.mvp.view.viewimp;

import android.databinding.ViewDataBinding;

import com.ymr.common.ui.view.SureDialog;
import com.ymr.mvp.presenter.LoadDataPresenter;
import com.ymr.mvp.view.ILoadDataView;

/**
 * Created by ymr on 15/11/12.
 */
public abstract class LoadDataFragmentView<P extends LoadDataPresenter> extends DataBindingFragmentView implements ILoadDataView {

    private LoadDataActivityViewDelegate<P> mDelegate;

    @Override
    public void onCreateDataBinding(ViewDataBinding databinding) {
        mDelegate = new LoadDataActivityViewDelegate<>(onCreatePresenter());
        finishCreatePresenter(databinding);
    }

    protected abstract void finishCreatePresenter(ViewDataBinding databinding);

    protected abstract P onCreatePresenter();

    protected P getPresenter() {
        return mDelegate.getPresenter();
    }

    @Override
    public void onError(String msg) {
        mDelegate.onError(msg);
    }

    @Override
    public void onMessage(String msg) {
        mDelegate.onMessage(msg);
    }

    @Override
    public void showLoading() {
        mDelegate.showLoading();
    }

    @Override
    public void showLoading(long delay) {
        mDelegate.showLoading(delay);
    }

    @Override
    public void hideLoading() {
        mDelegate.hideLoading();
    }

    @Override
    public void showSureDialog(Runnable okRun, String content) {
        mDelegate.showSureDialog(okRun,content);
    }

    @Override
    public void showSureDialog(Runnable okRun, String content, SureDialog.SureDialogListener listener) {
        mDelegate.showSureDialog(okRun, content,listener);
    }

    @Override
    public void setTimeOut(long timeOut) {
        mDelegate.setTimeOut(timeOut);
    }
}
