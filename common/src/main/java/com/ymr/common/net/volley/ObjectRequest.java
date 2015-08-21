package com.ymr.common.net.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ymr.common.bean.ApiBase;
import com.ymr.common.util.LOGGER;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ymr on 15/6/25.
 */
public class ObjectRequest<T> extends StringRequest {

    private static final String TAG = "ObjectRequest";

    public ObjectRequest(int method, final String url, final Response.Listener<ApiBase<T>> listener, Response.ErrorListener errorListener, final Class<T> tClass) {
        super(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ParseUtil.generateObject(s, url, tClass, listener);
            }
        }, errorListener);
    }

    public ObjectRequest(final String url, final Response.Listener<ApiBase<T>> listener, Response.ErrorListener errorListener, final Class<T> tClass) {
        super(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ParseUtil.generateObject(s, url, tClass, listener);
            }
        }, errorListener);
    }
}
