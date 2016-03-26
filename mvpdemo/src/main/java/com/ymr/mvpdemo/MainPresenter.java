package com.ymr.mvpdemo;

import com.ymr.supermvp.android.databinding.DatabindingPresenter;

/**
 * Created by ymr on 16/3/26.
 */
public class MainPresenter extends DatabindingPresenter<MainView> {
    @Override
    public void finishCreateView() {

    }

    private void updateView() {
        if (getView().isHelloShowing()) {
            getView().setBtnText("hide");
        } else {
            getView().setBtnText("show");
        }
    }

    public void clickShow() {
        if (getView().isHelloShowing()) {
            getView().hideHello();
        } else {
            getView().showHello();
        }
        updateView();
    }
}
