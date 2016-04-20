package com.ymr.mvp.view.viewimp;

import android.databinding.ViewDataBinding;
import android.view.KeyEvent;

import com.ymr.common.ui.view.SureDialog;
import com.ymr.mvp.presenter.LoadDataPresenter;
import com.ymr.mvp.view.ILoadDataView;

/**
 * Created by ymr on 15/11/12.
 */
public abstract class LoadDataActivityView<P extends LoadDataPresenter> extends DataBindingActivityView implements ILoadDataView<P>{

    private LoadDataViewDelegate<P> mDelegate;

    @Override
    public void onCreateDataBinding(ViewDataBinding databinding) {
        mDelegate = new LoadDataViewDelegate<>(onCreatePresenter());
        finishCreatePresenter(databinding);
    }

    protected abstract void finishCreatePresenter(ViewDataBinding databinding);

    protected abstract P onCreatePresenter();

    @Override
    public P getPresenter() {
        if (mDelegate != null) {
            return mDelegate.getPresenter();
        }
        return null;
    }

    @Override
    public void onError(String msg) {
        if (mDelegate != null) {
            mDelegate.onError(msg);
        }
    }

    @Override
    public void onMessage(String msg) {
        if (mDelegate != null) {
            mDelegate.onMessage(msg);
        }
    }

    @Override
    public void showLoading() {
        if (mDelegate != null) {
            mDelegate.showLoading();
        }
    }

    @Override
    public void showLoading(long delay) {
        if (mDelegate != null) {
            mDelegate.showLoading(delay);
        }
    }

    @Override
    public void hideLoading() {
        if (mDelegate != null) {
            mDelegate.hideLoading();
        }
    }

    @Override
    public void showSureDialog(Runnable okRun, String content) {
        if (mDelegate != null) {
            mDelegate.showSureDialog(okRun,content);
        }
    }

    @Override
    public void showSureDialog(Runnable okRun, String content, SureDialog.SureDialogListener listener) {
        if (mDelegate != null) {
            mDelegate.showSureDialog(okRun, content, listener);
        }
    }

    @Override
    public void setTimeOut(long timeOut) {
        if (mDelegate != null) {
            mDelegate.setTimeOut(timeOut);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDelegate != null) {
            mDelegate.onDestroy();
        }
    }

    @Override
    public boolean onActionbarBackPressed() {
        if (mDelegate != null) {
            return mDelegate.backPressed();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDelegate != null) {
                if (!mDelegate.backPressed()) {
                    return super.onKeyDown(keyCode, event);
                }
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
