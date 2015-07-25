package com.ymr.common.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymr.common.R;


/**
 * Created by ymr on 15/6/25.
 */
public class BaseUIController<T extends Activity & BaseUI> implements View.OnClickListener {

    protected T mActivity;
    private TextView mTitle;
    private ImageView mBack;

    private static BaseUIParams sBaseUIParams;
    private BaseUIParams mBaseUIParams;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back) {
            mActivity.finish();
        }
    }

    public interface BaseUIParams {
        int getTitleBgColor();
        int getTitleTextColor();
        int getBackDrawable();
    }

    public BaseUIController(T activity) {
        mActivity = activity;
        mBaseUIParams = mActivity.getBaseUIParams();
    }

    public static void initBaseUIParams(BaseUIParams baseUIParams) {
        sBaseUIParams = baseUIParams;
    }

    public BaseUIParams getBaseUIParams() {
        return mBaseUIParams == null ? sBaseUIParams : mBaseUIParams;
    }

    public void initActivity() {
        mActivity.onStartCreatView();
        mActivity.setContentView(R.layout.activity_base);
        onInitViews();
        mActivity.onFinishCreateView();
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

        mActivity.findViewById(R.id.title_container).setBackgroundColor(getBaseUIParams().getTitleBgColor());

        //get the layout of sub activity
        ViewStub viewStub = (ViewStub) mActivity.findViewById(R.id.sub_activity_content);
        viewStub.setLayoutResource(mActivity.getContentViewId());
        viewStub.inflate();
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    void setBaseUIParams(BaseUIParams baseUIParams) {
        mBaseUIParams = baseUIParams;
    }

    public T getActivity() {
        return mActivity;
    }
}
