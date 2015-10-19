package com.ymr.mvp.presenter;


import com.ymr.common.R;
import com.ymr.common.Statistical;
import com.ymr.common.util.DeviceInfoUtils;
import com.ymr.common.util.StatisticalHelper;
import com.ymr.mvp.view.IView;

/**
 * Created by ymr on 15/8/29.
 */
public class BasePresenter<V extends IView> implements Statistical{

    private final V mView;

    public BasePresenter(V view) {
        mView = view;
    }

    public V getView() {
        return mView;
    }

    public boolean verifyInternet() {
        if (!DeviceInfoUtils.hasInternet(mView.getActivity())) {
            mView.onMessage(mView.getActivity().getString(R.string.has_no_internet));
            return false;
        }
        return true;
    }

    @Override
    public void writeToStatistical(String actionType) {
        StatisticalHelper.doStatistical(mView.getActivity(), actionType);
    }
}
