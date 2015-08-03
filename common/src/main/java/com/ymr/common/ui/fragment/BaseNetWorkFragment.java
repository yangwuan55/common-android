package com.ymr.common.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ymr.common.R;
import com.ymr.common.net.LoadStateListener;
import com.ymr.common.net.NetWorkController;
import com.ymr.common.net.NetWorkModel;
import com.ymr.common.net.params.NetRequestParams;
import com.ymr.common.ui.view.LoadingView;

import static com.ymr.common.net.LoadStateListener.NetworkLoadStatus.*;

/**
 * Created by ymr on 15/5/14.
 */
public abstract class BaseNetWorkFragment<T> extends Fragment implements  NetWorkModel.NetworkChangedListener, LoadStateListener<T> {
    private View mRootView;
    private FrameLayout mFlytChildRoot;
    //private LoadingView mLoadingView;
    private TextView mTvError;
    private Context mContext;

    private NetworkLoadStatus mCurrentLoadStatus = LOAD_IDEL;
    private NetWorkController<T> mNetWorkController;
    private int mErroDrawableId;
    private String mErrorText;

    protected abstract NetWorkModel<T> getNetWorkModel(Context context);

    protected abstract NetRequestParams getParams();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mNetWorkController = new NetWorkController<>(getActivity(),getNetWorkModel(mContext));
        mNetWorkController.setLoadStateListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_network_base, null);
        mFlytChildRoot = (FrameLayout) mRootView.findViewById(R.id.flyt_fragment_container);
        //mLoadingView = (LoadingView) mRootView.findViewById(R.id.loading_view_base_fragment);
        mTvError = (TextView) mRootView.findViewById(R.id.tv_network_error);

        if (mErroDrawableId != 0) {
            mTvError.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(mErroDrawableId), null, null);
        }

        if (!TextUtils.isEmpty(mErrorText)) {
            mTvError.setText(mErrorText);
        }

        mTvError.setOnClickListener(reloadListener);
        mFlytChildRoot.addView(getChildRootView(inflater));
        return mRootView;
    }

    protected void setErorrDrawableId(int drawableId) {
        mErroDrawableId = drawableId;
        if (mTvError != null) {
            mTvError.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
        }
    }

    protected void setErrorText(String errorText) {
        mErrorText = errorText;
        if (mTvError != null) {
            mTvError.setText(errorText);
        }
    }

    /**
     * 子类实现此方法，等同于实现自己的onCreateView()
     * @param inflater
     * @return
     */
    protected abstract View getChildRootView(LayoutInflater inflater);

    protected void updateData() {
        updateData(getParams());
    }

    protected void updateData(NetRequestParams params) {
        mNetWorkController.updateData(params);
    }


    protected void setLoadStatus(NetworkLoadStatus loadStatus) {
        mCurrentLoadStatus = loadStatus;
        updateDisplay();
    }

    protected NetworkLoadStatus getCurrentLoadStatus(){
        return mCurrentLoadStatus;
    }

    private void updateDisplay() {
        if (onInterceptDisplay()) {
            return;
        }
        switch (mCurrentLoadStatus) {
            case LOAD_IDEL:
            case LOAD_START:
                mTvError.setVisibility(View.GONE);
                //mLoadingView.setVisibility(View.VISIBLE);
                break;
            case LOAD_FAIL:
            case LOAD_NETWORK_ERROR:
                mTvError.setVisibility(View.VISIBLE);
                //mLoadingView.setVisibility(View.GONE);
                break;
            case LOAD_FINISH:
                mTvError.setVisibility(View.GONE);
                //mLoadingView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 是否拦截状态更新显示
     * @return
     */
    private boolean onInterceptDisplay() {
        if (mTvError == null /*|| mLoadingView == null*/) {
            return true;
        }
        if (interceptDisplay()) {
            return true;
        }
        return false;
    }

    protected boolean interceptDisplay() {
        return false;
    }

    private View.OnClickListener reloadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateData(getParams());
        }
    };

    @Override
    public void onNetworkChange() {
        updateData();
    }

    @Override
    public void onStateChange(NetworkLoadStatus state) {
        setLoadStatus(state);
    }
}
