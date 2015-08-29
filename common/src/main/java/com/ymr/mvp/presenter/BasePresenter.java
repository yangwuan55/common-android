package com.ymr.mvp.presenter;


import com.ymr.common.R;
import com.ymr.common.util.DeviceInfoUtils;
import com.ymr.mvp.view.IView;

/**
 * Created by ymr on 15/8/29.
 */
public class BasePresenter {

    private final IView mView;

    public BasePresenter(IView view) {
        mView = view;
    }

    public boolean verifyInternet() {
        if (!DeviceInfoUtils.hasInternet(mView.getActivity())) {
            mView.onMessage(mView.getActivity().getString(R.string.has_no_internet));
            return false;
        }
        return true;
    }

}
