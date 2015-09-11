package com.ymr.common.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.ymr.common.R;


/**
 * Created by ymr on 15/6/25.
 */
public class WebViewActivity extends BaseActivity {

    public static final String URL = "url";
    public static final String TITLE_NAME = "title_name";
    private String mUrl;
    private String mTitleName;
    private WebViewController mWebViewController;

    @Override
    public boolean hasBack() {
        return true;
    }

    @Override
    public void onFinishCreateView() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(URL);
        mTitleName = intent.getStringExtra(TITLE_NAME);
        if (!TextUtils.isEmpty(mTitleName)) {
            setTitle(mTitleName);
        }
        mWebViewController = new WebViewController((ViewGroup) findViewById(R.id.web_view_container),R.layout.webview_content);
        mWebViewController.loadUrl(mUrl);
    }

    @Override
    public String getTitleText() {
        return "webview";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_web_view;
    }

    public void setJavascriptInterface(WebViewController.JavascriptInterface javascriptInterface) {
        mWebViewController.setJavascriptInterface(javascriptInterface);
    }

    public void callToJs(String params) {
        mWebViewController.callToJs(params);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && interceptBackPress()) {
            if (!mWebViewController.onBackPressed()) {
                return super.onKeyDown(keyCode,event);
            } else {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean interceptBackPress() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebViewController.close();
    }
}
