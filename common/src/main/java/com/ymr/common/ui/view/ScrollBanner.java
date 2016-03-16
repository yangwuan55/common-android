package com.ymr.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ymr.common.R;
import com.ymr.common.ui.adapter.ScrollBannerAdapter;

public class ScrollBanner extends FrameLayout implements View.OnClickListener, ScrollBannerAdapter.BannerDataListener {
    private final static int MESSAGE_SCOLL = 1;
    private final static long DELAY = 4500;
    private LinearLayout mIndicatorLinearLayout;
    private Context mContext;
    private int mAdapterRealCount;
    private ScrollBannerAdapter mScrollBannerAdapter;
    private ImageView mBtnClose;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCOLL:
                    nextPage();
                    mHandler.removeMessages(MESSAGE_SCOLL);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_SCOLL, DELAY);
                    break;
            }
        }

    };

    private ViewPager mViewPager;

    public ScrollBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mViewPager = new ViewPager(context, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(lp);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPage = -1;

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (mAdapterRealCount > 0) {
                    refreshIndicatorPosition(i % mAdapterRealCount);
                    currentPage = i;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        this.addView(mViewPager);
        initCloseBtn(context,attrs);
    }

    private void initCloseBtn(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollBanner);
        boolean isShowClose = a.getBoolean(R.styleable.ScrollBanner_show_close, false);
        if(isShowClose) {
            mBtnClose = new ImageView(context, attrs);
            LayoutParams closeLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            closeLp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
            closeLp.rightMargin = 20;
            mBtnClose.setLayoutParams(closeLp);
            mBtnClose.setImageResource(R.drawable.ad_close);
            addView(mBtnClose);
            mBtnClose.setOnClickListener(this);
        }
    }

    private void initIndicator(int size) {
        if (mIndicatorLinearLayout == null) {
            mIndicatorLinearLayout = new LinearLayout(mContext);
            mIndicatorLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams fl = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            fl.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            fl.setMargins(0, 0, 0, 18);
            mIndicatorLinearLayout.setLayoutParams(fl);
            mIndicatorLinearLayout.setGravity(Gravity.CENTER);
            addView(mIndicatorLinearLayout);
        } else {
            mIndicatorLinearLayout.removeAllViews();
        }
        mAdapterRealCount = size;
        for (int i = 0; i < size; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setImageResource(R.drawable.indicator_other);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            iv.setLayoutParams(ll);
            iv.setPadding(7, 0,7, 0);
            mIndicatorLinearLayout.addView(iv);
        }
    }

    public void nextPage() {
        int page = mViewPager.getCurrentItem() + 1;
        mViewPager.setCurrentItem(page, true);
    }

    private void refreshIndicatorPosition(int current) {
        int childCount = mIndicatorLinearLayout.getChildCount();
        for (int i = 0; i < childCount;i ++) {
            ImageView child = (ImageView) mIndicatorLinearLayout.getChildAt(i);
            child.setImageResource(R.drawable.indicator_other);
        }
        if (childCount > 0) {
            ((ImageView) mIndicatorLinearLayout.getChildAt(current)).setImageResource(R.drawable.indicator_current);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility == ViewPager.VISIBLE) {
            if (mScrollBannerAdapter != null && mScrollBannerAdapter.getCount() > 1) {
                mHandler.sendEmptyMessage(MESSAGE_SCOLL);
            }
        }else{
            mHandler.removeMessages(MESSAGE_SCOLL);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        mHandler.removeMessages(MESSAGE_SCOLL);
        mHandler.sendEmptyMessageDelayed(MESSAGE_SCOLL, DELAY);
        return super.onTouchEvent(arg0);
    }

    public void setAdapter(ScrollBannerAdapter adapter) {
        mScrollBannerAdapter = adapter;
        if (mScrollBannerAdapter != null) {
            mScrollBannerAdapter.setBannerListener(this);
            mViewPager.setAdapter(mScrollBannerAdapter);
            updateBanner();
        }
    }

    private void updateBanner() {
        if (mScrollBannerAdapter != null && mScrollBannerAdapter.getCount() > 1) {
            //mViewPager.setCurrentItem(500, false);
            refreshIndicator();
        }
    }

    public void refreshIndicator() {
        if (mScrollBannerAdapter != null && mScrollBannerAdapter.getCount() > 1) {
            mHandler.sendEmptyMessage(MESSAGE_SCOLL);
            initIndicator(mScrollBannerAdapter.getRealCount());
            refreshIndicatorPosition(0);
        }
    }

    public void startScoll() {
        if (mScrollBannerAdapter != null && mScrollBannerAdapter.getCount() > 1) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_SCOLL, DELAY);
        }
    }

    public void stopScoll() {
        mHandler.removeMessages(MESSAGE_SCOLL);
    }

    @Override
    public void onClick(View view) {
        if(view == mBtnClose){
            setVisibility(View.GONE);
        }
    }

    @Override
    public void onDataChanged() {
        updateBanner();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopScoll();
    }
}
