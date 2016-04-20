package com.ymr.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.internal.LoadingLayout;
import com.ymr.common.Env;

/**
 * Created by ymr on 16/4/20.
 */
public class CustomLoadingPulltorefreshListView extends PullToRefreshListView {
    public CustomLoadingPulltorefreshListView(Context context) {
        super(context);
    }

    public CustomLoadingPulltorefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLoadingPulltorefreshListView(Context context, Mode mode) {
        super(context, mode);
    }

    public CustomLoadingPulltorefreshListView(Context context, Mode mode, Class<? extends LoadingLayout> loadingLayoutClazz) {
        super(context, mode, loadingLayoutClazz);
    }

    @Override
    protected LoadingLayout createLoadingLayout(Context context, Mode mode, TypedArray attrs) {
        if (Env.sListViewLoadingLayout != null) {
            return Env.sListViewLoadingLayout.getLoadingLayout(context,mode,attrs);
        } else {
            return super.createLoadingLayout(context, mode, attrs);
        }
    }
}
