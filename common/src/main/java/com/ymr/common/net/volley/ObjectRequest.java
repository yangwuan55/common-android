package com.ymr.common.net.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.ymr.common.bean.IApiBase;

/**
 * Created by ymr on 15/6/25.
 */
public class ObjectRequest<D> extends StringRequest {

    private static final String TAG = "ObjectRequest";

    public ObjectRequest(int method, final String url, final Response.Listener<IApiBase<D>> listener, final Response.ErrorListener errorListener, final Class<D> tClass) {
        super(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ParseUtil.generateObject(s, url, tClass, listener, errorListener);
            }
        }, errorListener);
    }

    public ObjectRequest(final String url, final Response.Listener<IApiBase<D>> listener, final Response.ErrorListener errorListener, final Class<D> tClass) {
        super(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ParseUtil.generateObject(s, url, tClass, listener, errorListener);
            }
        }, errorListener);
    }
}
