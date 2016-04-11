package com.ymr.mvp.view;


import com.ymr.common.ui.view.SureDialog;
import com.ymr.mvp.presenter.LoadDataPresenter;

/**
 * Created by ymr on 15/11/12.
 */
public interface ILoadDataView<P extends LoadDataPresenter> extends INetView {
    P getPresenter();

    void showLoading();

    void showLoading(long delay);

    void hideLoading();

    void showSureDialog(Runnable okRun, String content);

    void showSureDialog(Runnable okRun, String content, SureDialog.SureDialogListener listener);

    void setTimeOut(long timeOut);
}
