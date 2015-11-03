package com.ymr.mvp.view.viewimp;

import android.app.ProgressDialog;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.ymr.common.R;
import com.ymr.common.databinding.LayoutBaseListViewBinding;
import com.ymr.common.ui.adapter.DataBaseAdapter;
import com.ymr.common.util.ToastUtils;
import com.ymr.mvp.model.bean.IListItemBean;
import com.ymr.mvp.presenter.ListPresenterHasEmpty;
import com.ymr.mvp.view.IListViewHasEmptyView;

import java.util.List;

/**
 * Created by ymr on 15/10/17.
 */
public abstract class MvpListViewFragment<D, E extends IListItemBean<D>> extends DataBindingFragmentView implements IListViewHasEmptyView<D,E> {

    private LayoutBaseListViewBinding mDatabinding;
    private ListPresenterHasEmpty<D,E,? extends IListViewHasEmptyView<D,E>> mListPresenter;
    private DataBaseAdapter<D> mAdapter;
    private ProgressDialog mProgressDialog;
    private Handler mHandler = new Handler();

    @Override
    public int getContentViewId() {
        return R.layout.layout_base_list_view;
    }

    @Override
    public void onCreateDataBinding(ViewDataBinding databinding) {
        mListPresenter = createPresenter();
        mDatabinding = ((LayoutBaseListViewBinding) databinding);
        Drawable backGround = getBackGround();
        if (backGround != null) {
            mDatabinding.getRoot().setBackgroundDrawable(backGround);
        }
        mDatabinding.list.setMode(PullToRefreshBase.Mode.BOTH);
        mDatabinding.list.getRefreshableView().setDivider(null);
        mDatabinding.list.getRefreshableView().setSelector(android.R.color.transparent);
        mDatabinding.list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mListPresenter.onRefreshFromTop();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mListPresenter.onRefreshFromBottom();
            }
        });
        mDatabinding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MvpListViewFragment.this.onItemClick(parent, view, position, id);
            }
        });
        mAdapter = getAdapter();
        mDatabinding.list.setAdapter(mAdapter);
        mDatabinding.emptyView.addView(getEmptyView());

        mListPresenter.loadDatas();
    }

    protected abstract Drawable getBackGround();

    protected abstract View getEmptyView();

    protected abstract ListPresenterHasEmpty<D,E,? extends IListViewHasEmptyView<D,E>> createPresenter();

    protected abstract DataBaseAdapter<D> getAdapter();

    protected abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);

    @Override
    public void setDatas(List<D> datas) {
        mAdapter.setDatas(datas);
    }

    @Override
    public void addDatas(List<D> datas) {
        mAdapter.addDatas(datas);
    }

    @Override
    public void setBottomRefreshEnable(boolean enable) {
        if (enable) {
            mDatabinding.list.setMode(PullToRefreshBase.Mode.BOTH);
        } else {
            mDatabinding.list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
    }

    @Override
    public void compliteRefresh() {
        mDatabinding.list.onRefreshComplete();
    }

    @Override
    public void setRefreshEnable(boolean enable) {
        mDatabinding.list.setEnabled(enable);
    }

    @Override
    public void showEmptyView() {
        mDatabinding.emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        mDatabinding.emptyView.setVisibility(View.GONE);
    }

    @Override
    public List<D> getCurrDatas() {
        return mAdapter.getDatas();
    }

    @Override
    public void startRefresh() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.show();
    }

    @Override
    public void finishRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        }, 800);
    }

    @Override
    public void onMessage(String msg) {
        ToastUtils.showToast(getContext(), msg);
    }

    @Override
    public void onError(String msg) {
        onMessage(msg);
    }
}
