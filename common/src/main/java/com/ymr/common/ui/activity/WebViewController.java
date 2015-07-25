package com.ymr.common.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ymr.common.R;

/**
 * Created by ymr on 15/7/3.
 */
public class WebViewController implements View.OnClickListener {

    private static final int SHOW_ERROR = 0;
    private final WebView mWebView;
    private final View mErrorView;
    private String mUrl;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case SHOW_ERROR:
                    mErrorView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


    public WebViewController(ViewGroup webViewConainer,int webviewContentId) {
        View.inflate(webViewConainer.getContext(),webviewContentId,webViewConainer);
        mWebView = (WebView) webViewConainer.findViewById(R.id.web_view);
        mErrorView = webViewConainer.findViewById(R.id.error_view);
        mErrorView.setOnClickListener(this);
        initWebview();
    }

    public void initWebview() {
        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        /**
         * 禁用缓存
         */
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 启用定位
        settings.setGeolocationEnabled(true);
        // 启用文件内的WebView访问
        settings.setAllowFileAccess(true);
        // 启用数据库的存储API
        settings.setDatabaseEnabled(true);
        // 启用DOM存储API
        settings.setDomStorageEnabled(true); // 允许html localStorage
        // 设置默认html编码
        settings.setDefaultTextEncodingName("utf-8");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showError();
            }
        });
    }

    private void showError() {
        mHandler.sendEmptyMessage(SHOW_ERROR);
    }

    public void loadUrl(String url) {
        mUrl = url;
        mWebView.loadUrl(url);
        mErrorView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        reload();
    }

    private void reload() {
        loadUrl(mUrl);
    }
}
