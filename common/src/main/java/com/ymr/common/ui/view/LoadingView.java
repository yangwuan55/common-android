package com.ymr.common.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ymr.common.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taozui on 15/5/19.
 */
public class LoadingView extends RelativeLayout {
    private static final int STEP_1 = 0;
    private static final int STEP_2 = 1;
    private static final int STEP_3 = 2;
    private static final int STEP_4 = 3;
    private ImageView mRed, mBlue, mGreen, mPurple;
    private static final int CENTERBUFFER = 10;
    private static final long DURATION = 800;


    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(LoadingView.this.getVisibility() == View.VISIBLE) {
                repeat(msg.what);
            }
        }
    };

    public LoadingView(Context context) {
        super(context);
        initView(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context,AttributeSet attrs){
        initLoadingView(context, attrs);
    }

    private void initLoadingView(Context context, AttributeSet attrs) {
        mRed = new ImageView(context, attrs);
        mBlue = new ImageView(context, attrs);
        mGreen = new ImageView(context, attrs);
        mPurple = new ImageView(context, attrs);
        mRed = new ImageView(context, attrs);
        mBlue = new ImageView(context, attrs);
        mGreen = new ImageView(context, attrs);
        mPurple = new ImageView(context, attrs);

        mRed.setImageResource(R.drawable.circle_red);
        mBlue.setImageResource(R.drawable.circle_blue);
        mGreen.setImageResource(R.drawable.circle_green);
        mPurple.setImageResource(R.drawable.circle_purple);
        addView(mRed);
        addView(mBlue);
        addView(mGreen);
        addView(mPurple);
        setGravity(Gravity.CENTER);
    }

    public void play() {
        this.post(new Runnable() {
            @Override
            public void run() {
                mHander.sendEmptyMessage(STEP_1);
            }
        });

    }

    public void stop() {
        mHander.removeMessages(STEP_1);
        mHander.removeMessages(STEP_2);
        mHander.removeMessages(STEP_3);
        mHander.removeMessages(STEP_4);
        clearAnimation();
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            play();
        } else {
            stop();
        }
        super.setVisibility(visibility);
    }

    private void addAnimatorToList(View view, List<Animator> list, int step) {
        int startLeftX = getWidth() / 2 - mPurple.getWidth() - CENTERBUFFER;
        int startTopY = getHeight() / 2 - mPurple.getHeight() - CENTERBUFFER;
        int startRightX = getWidth() / 2 + CENTERBUFFER;
        int startBottomY = getHeight() / 2 + CENTERBUFFER;
        ObjectAnimator x = null;
        ObjectAnimator y = null;
        switch (step) {
            case STEP_1:
                x = ObjectAnimator.ofFloat(view, "x",
                        startLeftX, startLeftX + CENTERBUFFER * 0.8f, startLeftX).setDuration(DURATION);
                y = ObjectAnimator.ofFloat(view, "y",
                        startTopY, startTopY + CENTERBUFFER * 0.8f, startTopY).setDuration(DURATION);
                break;
            case STEP_2:
                x = ObjectAnimator.ofFloat(view, "x",
                        startRightX, startRightX - CENTERBUFFER * 0.8f, startRightX).setDuration(DURATION);
                y = ObjectAnimator.ofFloat(view, "y",
                        startTopY, startTopY + CENTERBUFFER * 0.8f, startTopY).setDuration(DURATION);
                break;
            case STEP_3:
                x = ObjectAnimator.ofFloat(view, "x",
                        startRightX, startRightX - CENTERBUFFER * 0.8f, startRightX).setDuration(DURATION);
                y = ObjectAnimator.ofFloat(view, "y",
                        startBottomY, startBottomY - CENTERBUFFER * 0.8f, startBottomY).setDuration(DURATION);
                break;
            case STEP_4:
                x = ObjectAnimator.ofFloat(view, "x",
                        startLeftX, startLeftX + CENTERBUFFER * 0.8f, startLeftX).setDuration(DURATION);
                y = ObjectAnimator.ofFloat(view, "y",
                        startBottomY, startBottomY - CENTERBUFFER * 0.8f, startBottomY).setDuration(DURATION);
                break;

        }
        list.add(x);
        list.add(y);
    }


    private void repeat(final int step) {
        ObjectAnimator animatorRotate = ObjectAnimator.ofFloat(LoadingView.this, "rotation", 0, 90).setDuration(DURATION);
        AnimatorSet set = new AnimatorSet();
        ArrayList<Animator> list = new ArrayList<>();
        switch (step) {
            case STEP_1:
                addAnimatorToList(mPurple, list, STEP_1);
                addAnimatorToList(mRed, list, STEP_2);
                addAnimatorToList(mBlue, list, STEP_3);
                addAnimatorToList(mGreen, list, STEP_4);
                break;
            case STEP_2:
                addAnimatorToList(mPurple, list, STEP_2);
                addAnimatorToList(mRed, list, STEP_3);
                addAnimatorToList(mBlue, list, STEP_4);
                addAnimatorToList(mGreen, list, STEP_1);
                break;
            case STEP_3:
                addAnimatorToList(mPurple, list, STEP_3);
                addAnimatorToList(mRed, list, STEP_4);
                addAnimatorToList(mBlue, list, STEP_1);
                addAnimatorToList(mGreen, list, STEP_2);
                break;
            case STEP_4:
                addAnimatorToList(mPurple, list, STEP_4);
                addAnimatorToList(mRed, list, STEP_1);
                addAnimatorToList(mBlue, list, STEP_2);
                addAnimatorToList(mGreen, list, STEP_3);
                break;
        }

        list.add(animatorRotate);
        set.playTogether(list);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mHander.sendEmptyMessageDelayed((step + 1) % 4, 300L);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();
    }

}
