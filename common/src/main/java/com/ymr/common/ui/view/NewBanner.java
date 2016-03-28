package com.ymr.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.ymr.common.R;

import java.util.List;


/**
 * Created by bigpeach on 16/3/28.
 */
public class NewBanner<T> extends FrameLayout implements View.OnClickListener, com.bigkoo.convenientbanner.listener.OnItemClickListener {
    private final static long DEFAULT_TIME_BUFFER = 4500;
    private Context mContext;
    private ImageView mBtnClose;
    private ConvenientBanner mBanner;
    private List<T> mData;
    private long mBufferTime = DEFAULT_TIME_BUFFER;
    private NewBanner.OnItemClickListener mItemClickListener;
    private OnDataSetListener mDataSetListener;
    public interface PageIndicatorAlign {
        int LEFT = 1;
        int RIGHT = 2;
        int MIDDLE = 3;
    }

    public interface OnItemClickListener<T>{
        void onItemClick(T data,int posistion);
    }

    public interface OnDataSetListener<T>{
        int getBannerViewLayout();

        void updateUI(View view,T data);
    }

    public NewBanner(Context context) {
        super(context);
        initView(context,null);
    }

    public NewBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public NewBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context,AttributeSet attrs){
        mContext = context;
        mBanner = new ConvenientBanner(context,null);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mBanner.setLayoutParams(lp);
        mBanner.setOnItemClickListener(this);
        this.addView(mBanner);
        initCloseBtn(context, attrs);
    }

    private void initCloseBtn(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollBanner);
        boolean isShowClose = a.getBoolean(R.styleable.ScrollBanner_show_close, false);
        if(isShowClose) {
            mBtnClose = new ImageView(context, attrs);
            FrameLayout.LayoutParams closeLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            closeLp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
            closeLp.rightMargin = 20;
            mBtnClose.setLayoutParams(closeLp);
            mBtnClose.setImageResource(R.drawable.ad_close);
            addView(mBtnClose);
            mBtnClose.setOnClickListener(this);
        }
    }

    public void setLoopBufferTime(long time){
        mBufferTime = time;
    }

    public long getLoopBufferTime(){
        return mBufferTime;
    }


    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility == ViewPager.VISIBLE) {
            if (mData != null && mData.size() > 1) {
                mBanner.startTurning(getLoopBufferTime());
            }
        }else{
            mBanner.stopTurning();
        }

    }

    @Override
    public void onClick(View view) {
        if(view == mBtnClose){
            setVisibility(View.GONE);
        }
    }

    public void setBannerData(List<T> list){
        mData = list;
        mBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new LocalImageHolderView();
            }
        }, list);
        if(list == null || list.size() == 0){
            mBanner.setVisibility(View.GONE);
        }else{
            if(list.size() == 1){
                mBanner.setManualPageable(false);
                mBanner.setCanLoop(false);
            }else{
                mBanner.setManualPageable(true);
                mBanner.setCanLoop(true);

            }
            mBanner.setVisibility(View.VISIBLE);
            mBanner.notifyDataSetChanged();
        }
    }

    public void setItemClickListener(NewBanner.OnItemClickListener listener){
        mItemClickListener = listener;
    }

    @Override
    public void onItemClick(int position) {
        if(mItemClickListener != null){
            if(mData != null && mData.size() > position) {
                mItemClickListener.onItemClick(mData.get(position), position);
            }
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBanner.stopTurning();
    }

    public class LocalImageHolderView implements Holder<T> {
        private View mView;
        @Override
        public View createView(Context context) {
            int resId = 0;
            if(mDataSetListener != null){
                resId = mDataSetListener.getBannerViewLayout();
            }
            mView = LayoutInflater.from(context).inflate(resId,null);
            return mView;
        }

        @Override
        public void UpdateUI(Context context, final int position, T data) {
            if(mDataSetListener != null){
                mDataSetListener.updateUI(mView, data);
            }
        }
    }

    public void stopScroll(){
        mBanner.stopTurning();
    }

    public void startScroll(){
        if(mData != null && mData.size() > 1){
            mBanner.startTurning(mBufferTime);
        }
    }

    public void setOnDataSetListener(OnDataSetListener listener){
        mDataSetListener = listener;
    }

    public void setIndicatorRes(int[] indicator){
        mBanner.setPageIndicator(indicator);
    }

    public void setPageIndicatorAlign(int align){
        switch (align){
            case PageIndicatorAlign.LEFT:
                mBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_LEFT);
                break;
            case PageIndicatorAlign.RIGHT:
                mBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
                break;
            case PageIndicatorAlign.MIDDLE:
                mBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                break;
        }

    }

}
