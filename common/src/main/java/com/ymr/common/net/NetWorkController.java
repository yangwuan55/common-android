package com.ymr.common.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

/**
 * Created by ymr on 15/6/12.
 */
public class NetWorkController<D,P> {

    private static final String TAG = "NetWorkController";
    private final Context mContext;
    private LoadStateListener<D> mLoadStateListener;
    private P mParams;

    private NetWorkModel<D,P> mNetWorkModel;
    private NetWorkModel.UpdateListener<D> mUpdateListener = new NetWorkModel.UpdateListener<D>() {
        @Override
        public void finishUpdate(D result) {
            mLoadStateListener.onStateChange(LoadStateListener.NetworkLoadStatus.LOAD_FINISH);
            mLoadStateListener.onReceiveData(result);
        }

        @Override
        public void onError(String msg) {
            Log.e(TAG, "E:msg = " + msg);
            mLoadStateListener.onStateChange(LoadStateListener.NetworkLoadStatus.LOAD_FAIL);
        }

        @Override
        public void onError(VolleyError error) {
            Log.e(TAG, "E:error = " + error);
            mLoadStateListener.onStateChange(LoadStateListener.NetworkLoadStatus.LOAD_FAIL);
        }
    };

    public NetWorkController(Context context,NetWorkModel<D,P> netWorkModel) {
        mContext = context;
        setNetWorkModel(netWorkModel);
    }

    public void setLoadStateListener(LoadStateListener<D> loadStateListener) {
        this.mLoadStateListener = loadStateListener;
    }

    public void setNetWorkModel(NetWorkModel<D, P> netWorkModel) {
        this.mNetWorkModel = netWorkModel;
    }

    public void reload() {
        if (mParams != null) {
            if (NetUtil.checkNet(mContext)) {
                mLoadStateListener.onStateChange(LoadStateListener.NetworkLoadStatus.LOAD_START);
                mNetWorkModel.updateDatas(mParams, mUpdateListener);
            } else {
                mLoadStateListener.onStateChange(LoadStateListener.NetworkLoadStatus.LOAD_NETWORK_ERROR);
            }
        } else {
            mLoadStateListener.onStateChange(LoadStateListener.NetworkLoadStatus.LOAD_PARAMS_NULL);
        }
    }

    public void updateData(P params) {
        mParams = params;
        reload();
    }

    public void setParams(P params) {
        mParams = params;
    }
}
