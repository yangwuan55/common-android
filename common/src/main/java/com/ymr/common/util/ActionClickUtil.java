package com.ymr.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.ymr.common.bean.Action;
import com.ymr.common.ui.activity.WebViewActivity;

import java.io.Serializable;

/**
 * Created by bigpeach on 15/5/7.
 */
public class ActionClickUtil {

    public static final String EXT_PARAM = "extparam";

    public static final void doAction(Context context, Action action) {
        if (action == null) {
            return;
        }
        if (action.actiontype.equals(Constant.ActionBean.ACTION_TYPE_LOAD_PAGE)) {
            loadPage(context, action);
        } else if (action.actiontype.equals(Constant.ActionBean.ACTION_TYPE_VIDEO)) {
        }
    }

    private static final void launchNative(Context context, Action action) {
        Intent intent = getTargetIntent(context, action.pagetype);
        if (intent == null) {
            return;
        }
        Serializable extparam = action.getExtparam();
        if (extparam != null) {
            Bundle extras = new Bundle();
            extras.putSerializable(EXT_PARAM, extparam);
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    private static final Intent getTargetIntent(Context context, String target) {
        Class aClass = Constant.ActionBean.Target.TARGET_MAP.get(target);

        if (aClass != null) {
            return new Intent(context, aClass);
        }
        return null;
    }

    private static final void loadPage(Context context, Action<String> action) {
        if (action.pagetype.equals(Constant.ActionBean.PAGE_TYPE_LINK)) {
            launchWebView(context,action);
        } else if (action.pagetype.equals(Constant.ActionBean.PAGE_TYPE_LINK_BROWSER)){
            launchBrowser(context,action);
        } else {
            launchNative(context,action);
        }
    }

    private static void launchBrowser(Context context, Action<String> action) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri url = Uri.parse(action.getUrl());
        intent.setData(url);
        context.startActivity(intent);
    }

    private static void launchWebView(Context context, Action<String> action) {
        String url = action.getUrl();
        String title = action.getTitle();
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(title)) {
            LaunchWebView(context, url, title);
        }
    }

    public static void LaunchWebView(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.URL, url);
        intent.putExtra(WebViewActivity.TITLE_NAME, title);
        context.startActivity(intent);
    }

    public static Serializable getActionExtParam(Intent intent) {
        return intent.getExtras().getSerializable(ActionClickUtil.EXT_PARAM);
    }
}
