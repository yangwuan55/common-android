package com.ymr.common.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.ymr.common.R;
import com.ymr.common.net.DataReceiver;
import com.ymr.common.net.LoadStateListener;
import com.ymr.common.net.NetWorkController;
import com.ymr.common.net.params.NetRequestParams;

/**
 * Created by ymr on 15/7/7.
 */
public class NetWorkUIController<A extends Activity & NetWorkUI & DataReceiver,D> extends BaseUIController<A> implements LoadStateListener<D> {
    private final NetWorkController<D> mNetWorkController;
    private ViewGroup mErrorViewContainer;
    private NetworkLoadStatus mState;

    public NetWorkUIController(A activity) {
        super(activity);
        mNetWorkController = activity.getNetWorkController();
        mNetWorkController.setLoadStateListener(this);
    }

    @Override
    protected void onInitViews() {
        super.onInitViews();
        mErrorViewContainer = (ViewGroup) mActivity.findViewById(R.id.error_view_container);
        mErrorViewContainer.setOnClickListener(this);
    }

    public void updateData(NetRequestParams params) {
        mNetWorkController.updateData(params);
    }

    @Override
    public void onStateChange(NetworkLoadStatus state) {
        mState = state;
        updateUI();
    }

    private void showLoadFail() {
        if (mErrorViewContainer != null) {
            mErrorViewContainer.removeAllViews();
            View.inflate(mActivity, mActivity.getLoadFailViewId(), mErrorViewContainer);
            mErrorViewContainer.setVisibility(View.VISIBLE);
        }
    }

    private void showNetworkError() {
        if (mErrorViewContainer != null) {
            mErrorViewContainer.removeAllViews();
            View.inflate(mActivity, mActivity.getNetErorrViewId(), mErrorViewContainer);
            mErrorViewContainer.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI() {
        switch (mState) {
            case LOAD_PARAMS_NULL:
            case LOAD_FAIL:
                showLoadFail();
                break;

            case LOAD_FINISH:

                break;

            case LOAD_NETWORK_ERROR:
                showNetworkError();
                break;

            case LOAD_START:
                mErrorViewContainer.setVisibility(View.GONE);
                break;

            case LOAD_IDEL:

                break;
        }
    }

    @Override
    public void onReceiveData(D data) {
        mActivity.onReceiveData(data);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.error_view_container) {
            mNetWorkController.reload();
        }
    }
}
