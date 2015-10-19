package com.ymr.common.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ymr.common.Statistical;
import com.ymr.common.util.StatisticalHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class ScrollBannerAdapter<D> extends PagerAdapter implements Statistical {
    private List<D> mDataList;
    private Context mContext;
    private List<View> mViewList = new ArrayList<View>();
    private List<WeakReference<View.OnClickListener>> mClickListeners = new ArrayList<WeakReference<View.OnClickListener>>();

    private BannerDataListener mBannerListener;

    public interface BannerDataListener {
        void onDataChanged();
    }

    public void setBannerListener(BannerDataListener listener) {
        this.mBannerListener = listener;
    }

    public void addBannerClickListener(View.OnClickListener clickListener) {
        if (!contains(clickListener)) {
            mClickListeners.add(new WeakReference<View.OnClickListener>(clickListener));
        }
    }

    private void notifyClickListeners(View view) {
        for (WeakReference<View.OnClickListener> weakReference : mClickListeners) {
            View.OnClickListener onClickListener = weakReference.get();
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }

    private boolean contains(View.OnClickListener clickListener) {
        for (WeakReference<View.OnClickListener> weakReference : mClickListeners) {
            View.OnClickListener onClickListener = weakReference.get();
            if (clickListener.equals(onClickListener)) {
                return true;
            }
        }
        return false;
    }

    public ScrollBannerAdapter(Context context, List<D> list) {
        init(context);
        setDataList(list);
    }

    private void init(Context context) {
        mContext = context;
    }

    public ScrollBannerAdapter(Context context) {
        init(context);
    }

    public void setDataList(List<D> dataList) {
        this.mDataList = dataList;
        mViewList.clear();
        initView(mContext);
        if (mBannerListener != null) {
            mBannerListener.onDataChanged();
        }
    }


    private void initView(Context context) {
        if (mDataList == null || mDataList.isEmpty()) {
            return;
        }
        loadView(context);
        //tricky way for 2 view banner
        if (mDataList.size() == 3 || mDataList.size() == 2) {
            loadView(context);
        }
    }

    private void loadView(Context context) {
        for (D data : mDataList) {
            View view = LayoutInflater.from(context).inflate(getBannerViewLayout(), null);
            setViewData(data, view);
            view.setTag(data);
            view.setOnClickListener(mListener);
            mViewList.add(view);
        }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mViewList != null && !mViewList.isEmpty()) {
            View view = mViewList.get(position % mViewList.size());
            container.removeView(view);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View banner = null;
        if (mViewList != null && !mViewList.isEmpty()) {
            banner = mViewList.get(position%mViewList.size());
            if (banner.getParent() == container) {
                container.removeView(banner);
            }
            container.addView(banner);
        }
        return banner;
    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            D data = (D) view.getTag();
            onClickView(data,view);
            notifyClickListeners(view);
            writeToStatistical(BANNER+mDataList.indexOf(data));
        }
    };

    @Override
    public int getCount() {
        if (mViewList == null) {
            return 0;
        } else if (mViewList.size() == 1) {
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public int getRealCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public void writeToStatistical(String actionType) {
        StatisticalHelper.doStatistical(mContext,actionType);
    }

    /**
     * @param data
     * @param parent
     */
    protected abstract void setViewData(D data, View parent);

    /**
     * get the banner layout id
     * @return
     */
    protected abstract int getBannerViewLayout();

    /**
     * banner click event
     * @param data
     * @param view banner view
     */
    protected abstract void onClickView(D data,View view);
}
