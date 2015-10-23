package com.ymr.common.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymr.common.BaseApplication;
import com.ymr.common.R;
import com.ymr.common.util.Constant;


/**
 * Created by ymr on 15/6/25.
 */
public class BaseUIController<T extends Activity & BaseActivityUI> implements View.OnClickListener,IBaseUIController {

    protected T mActivity;
    private TextView mTitle;
    private ImageView mBack;

    private static BaseUIParams sBaseUIParams;
    private BaseUIParams mBaseUIParams;

    private BroadcastReceiver mExitBroadCast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_EXIST)) {
                mActivity.finish();
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back) {
            if (!mActivity.onActionbarBackPressed()) {
                mActivity.finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        mActivity.unregisterReceiver(mExitBroadCast);
        mActivity = null;
    }

    public BaseUIController(T activity) {
        BaseApplication.getRefWacher().watch(this);
        BaseApplication.getRefWacher().watch(activity);
        mActivity = activity;
        mBaseUIParams = mActivity.getBaseUIParams();
    }

    public static void initBaseUIParams(BaseUIParams baseUIParams) {
        sBaseUIParams = baseUIParams;
    }

    @Override
    public BaseUIParams getBaseUIParams() {
        return mBaseUIParams == null ? sBaseUIParams : mBaseUIParams;
    }

    @Override
    public void initActivity() {
        mActivity.onStartCreatView();
        mActivity.setContentView(R.layout.activity_base);
        onInitViews();
        mActivity.onFinishCreateView();

        IntentFilter filter = new IntentFilter(Constant.ACTION_EXIST);
        mActivity.registerReceiver(mExitBroadCast, filter);
    }

    protected void onInitViews() {
        mBack = (ImageView) mActivity.findViewById(R.id.back);
        if (mActivity.hasBack()) {
            mBack.setVisibility(View.VISIBLE);
            mBack.setOnClickListener(this);
            mBack.setImageResource(getBaseUIParams().getBackDrawable());
        } else {
            mBack.setVisibility(View.GONE);
        }

        int titleRightViewId = mActivity.getTitleRightViewId();
        if (titleRightViewId != 0) {
            ViewStub viewSub = (ViewStub) mActivity.findViewById(R.id.right_view);
            viewSub.setLayoutResource(titleRightViewId);
            View view = viewSub.inflate();
            View.OnClickListener onClickListener = mActivity.getTitleRightViewOnClickListener();
            if (onClickListener != null) {
                view.setOnClickListener(onClickListener);
            }
        }

        mTitle = (TextView) mActivity.findViewById(R.id.title);
        mTitle.setText(mActivity.getTitleText());
        mTitle.setTextColor(getBaseUIParams().getTitleTextColor());

        View actionbar = mActivity.findViewById(R.id.title_container);
        actionbar.setBackgroundColor(getBaseUIParams().getTitleBgColor());
        actionbar.setVisibility(mActivity.isActionBarVisible() ? View.VISIBLE : View.GONE);

        //get the layout of sub activity
        FrameLayout parent = (FrameLayout) mActivity.findViewById(R.id.sub_activity_content);
        inflateView(parent);
    }

    protected void inflateView(FrameLayout parent) {
        parent.addView(View.inflate(mActivity, mActivity.getContentViewId(), null));
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public T getActivity() {
        return mActivity;
    }
}
