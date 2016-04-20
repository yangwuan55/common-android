package com.ymr.common;

import android.content.Context;
import android.content.res.TypedArray;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.internal.LoadingLayout;

/**
 * Created by ymr on 16/4/20.
 */
public interface LoadingLayoutGetter {
    LoadingLayout getLoadingLayout(Context context, PullToRefreshBase.Mode mode, TypedArray attrs);
}
