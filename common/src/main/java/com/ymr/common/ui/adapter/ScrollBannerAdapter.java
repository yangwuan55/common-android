package com.ymr.common.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ymr.common.R;
import com.ymr.common.Statistical;
import com.ymr.common.bean.BannerData;
import com.ymr.common.net.volley.VolleyUtil;
import com.ymr.common.util.ActionClickUtil;
import com.ymr.common.util.StatisticalHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ScrollBannerAdapter extends PagerAdapter implements Statistical {
    private int mDefaultBannerId;
    private List<BannerData> mDataList;
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

    public ScrollBannerAdapter(Context context, List<BannerData> list,int defaultBannerId) {
        init(context, defaultBannerId);
        setDataList(list);
    }

    private void init(Context context, int defaultBannerId) {
        mContext = context;
        mDefaultBannerId = defaultBannerId;
    }

    public ScrollBannerAdapter(Context context,int defaultBannerId) {
        init(context, defaultBannerId);
    }

    public void setDataList(List<BannerData> dataList) {
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
        for (BannerData data : mDataList) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_banner_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_banner_background);
            VolleyUtil.getsInstance(mContext).loadImage(data.imageurl, imageView,mDefaultBannerId,mDefaultBannerId);
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
            BannerData data = (BannerData) view.getTag();
            ActionClickUtil.doAction(mContext, data.action);
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
}
