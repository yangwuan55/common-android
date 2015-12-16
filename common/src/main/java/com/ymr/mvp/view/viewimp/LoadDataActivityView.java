package com.ymr.mvp.view.viewimp;

import android.databinding.ViewDataBinding;
import android.view.KeyEvent;

import com.ymr.common.ui.view.SureDialog;
import com.ymr.mvp.presenter.LoadDataPresenter;
import com.ymr.mvp.view.ILoadDataView;

/**
 * Created by ymr on 15/11/12.
 */
public abstract class LoadDataActivityView<P extends LoadDataPresenter> extends DataBindingActivityView implements ILoadDataView{

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
        mDelegate.showSureDialog(okRun, content, listener);
    }

    @Override
    public void setTimeOut(long timeOut) {
        mDelegate.setTimeOut(timeOut);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDelegate.onDestroy();
    }

    @Override
    public boolean onActionbarBackPressed() {
        return mDelegate.backPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!mDelegate.backPressed()) {
                return super.onKeyDown(keyCode, event);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
