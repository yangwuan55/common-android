package com.ymr.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * Created by ymr on 15/5/15.
 */
public abstract class BaseCustomLayout extends FrameLayout {
    public BaseCustomLayout(Context context) {
        super(context);
    }

    public BaseCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseCustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, getStyleId());
        onTypedArrayCreated(a);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(getLayoutId(),this,true);
        onFinishAddView();
    }

    protected abstract int getLayoutId();

    protected int[] getStyleId() {
        return new int[0];
    }

    protected void onTypedArrayCreated(TypedArray a) {

    }

    protected abstract void onFinishAddView();

}
