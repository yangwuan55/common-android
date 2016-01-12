package com.ymr.appsearch.presenter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.ymr.appsearch.R;
import com.ymr.appsearch.bean.AppItem;
import com.ymr.appsearch.databinding.LayoutAppItemBinding;
import com.ymr.appsearch.domain.AppModel;
import com.ymr.appsearch.ui.viewinterface.IAppView;
import com.ymr.common.net.NetWorkModel;
import com.ymr.common.net.SimpleResultNetWorkModel;
import com.ymr.common.net.volley.VolleyUtil;
import com.ymr.common.ui.adapter.ListDataBindingAdapter;
import com.ymr.common.util.LOGGER;
import com.ymr.common.util.ToastUtils;
import com.ymr.mvp.presenter.LoadDataPresenter;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by ymr on 15/11/21.
 */
public class AppPresenter extends LoadDataPresenter<IAppView> {

    private static final String TAG = "AppPresenter";
    private final Activity mContext;
    private final PackageManager mPackageManager;
    private final AppModel mAppModel;
    private ListDataBindingAdapter<AppItem> mAppAdatper;
    private String mSearchNum = "";

    private Handler mHandler = new Handler();

    public AppPresenter(IAppView view) {
        super(view);
        mContext = getView().getActivity();
        mPackageManager = mContext.getPackageManager();
        mAppModel = new AppModel(getView().getActivity());
        mAppAdatper = new ListDataBindingAdapter<AppItem>(getView().getActivity()) {
            @Override
            protected DataBindingViewHolder createDataBindingViewHolder(int viewType) {
                return new DataBindingViewHolder() {
                    @Override
                    protected void reset(ViewDataBinding mDataBinding) {

                    }

                    @Override
                    protected void onReceiveData(AppItem data, ViewDataBinding dataBinding, int position) {
                        LayoutAppItemBinding appItemBinding = (LayoutAppItemBinding) dataBinding;
                        appItemBinding.appName.setText(data.getName());
                        VolleyUtil.getsInstance(getContext()).loadImage("file:/"+data.getImage(),appItemBinding.appIcon);
                    }

                    @Override
                    protected int getViewId() {
                        return R.layout.layout_app_item;
                    }
                };
            }
        };
    }

    public void loadDatas() {
        getView().showLoading();
        mAppModel.getAllAppItems(new NetWorkModel.UpdateListener<List<AppItem>>() {
            @Override
            public void finishUpdate(List<AppItem> result) {
                mAppAdatper.setDatas(result);
                getView().hideLoading();
            }

            @Override
            public void onError(NetWorkModel.Error error) {

            }
        });
        updateDatas();
    }

    public ListAdapter getAdapter() {
        return mAppAdatper;
    }

    public AdapterView.OnItemClickListener getItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openApp(mAppAdatper.getItem(position));
            }
        };
    }

    private void openApp(AppItem item) {
        Intent baseIntent = null;
        try {
            String intentStr = item.getBaseIntent();
            if (!TextUtils.isEmpty(intentStr)) {
                baseIntent = Intent.parseUri(intentStr, 0);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (baseIntent != null) {
            Intent intent = baseIntent;
            intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
                    | Intent.FLAG_ACTIVITY_TASK_ON_HOME
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.v(TAG, "Starting activity " + intent);
            try {
                mContext.startActivity(intent);
            } catch (SecurityException e) {
                Log.e(TAG, "Recents does not have the permission to launch "
                                + intent,
                        e);
                mContext.startActivity(mPackageManager.getLaunchIntentForPackage(
                        item.getPackageName()).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK));
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Error launching activity " + intent, e);
                mContext.startActivity(mPackageManager.getLaunchIntentForPackage(
                        item.getPackageName()).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK));
            }

        } else {
            try {
                mContext.startActivity(mPackageManager.getLaunchIntentForPackage(
                        item.getPackageName()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } catch (Exception e) {
                ToastUtils.showToast(getView().getActivity(),"应用未安装");
                reset();
            }
        }
        item.setOpenCount(item.getOpenCount()+1);
        mAppModel.updateAppItem(item, new SimpleResultNetWorkModel.SimpleRequestListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(NetWorkModel.Error error) {

            }
        });
    }

    private void update() {
        mAppModel.getAppItemsByNum(mSearchNum, new NetWorkModel.UpdateListener<List<AppItem>>() {
            @Override
            public void finishUpdate(List<AppItem> result) {
                mAppAdatper.setDatas(result);
                LOGGER.i(TAG,"mAppAdatper= " + mAppAdatper);
            }

            @Override
            public void onError(NetWorkModel.Error error) {

            }
        });
        if (!TextUtils.isEmpty(mSearchNum)) {
            getView().setTitle(mSearchNum);
        } else {
            getView().setTitle("t9");
        }
    }

    public void clickNum(CharSequence contentDescription) {
        mSearchNum += contentDescription;
        update();
    }

    public void delete() {
        LOGGER.i(TAG,"this = " + this);
        if (mSearchNum.length() <= 1) {
            mSearchNum = "";
        } else {
            mSearchNum = mSearchNum.substring(0,mSearchNum.length() - 1);
        }
        update();
    }

    public void longDelete() {
        reset();
    }

    private void reset() {
        mSearchNum = "";
        update();
    }

    public void onPause() {
        updateDatas();
    }

    private void updateDatas() {
        mAppModel.updateDatas(new NetWorkModel.UpdateListener<List<AppItem>>() {
            @Override
            public void finishUpdate(final List<AppItem> result) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        reset();
                    }
                });
            }

            @Override
            public void onError(NetWorkModel.Error error) {

            }
        });
    }
}
