package com.ymr.common.net.volley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;

/**
 * Created by dadeshan on 15/6/25.
 */
public class ObjectRequest<T> extends StringRequest {

    public ObjectRequest(int method, final String url, final Response.Listener<T> listener, Response.ErrorListener errorListener, final Class<T> tClass) {
        super(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                listener.onResponse(new GsonBuilder().create().fromJson(s,tClass));
            }
        }, errorListener);
    }

    public ObjectRequest(String url, final Response.Listener<T> listener, Response.ErrorListener errorListener, final Class<T> tClass) {
        super(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                listener.onResponse(new GsonBuilder().create().fromJson(s,tClass));
            }
        }, errorListener);
    }
}
