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
                generateObject(s, url, tClass, listener);
            }
        }, errorListener);
    }

    private static <T> void generateObject(String s, String url, Class<T> tClass, Response.Listener<ApiBase<T>> listener) {
        LOGGER.i(TAG, "URL = " + url);
        ParameterizedType type = null;
        if (tClass != null) {
            type = type(ApiBase.class, tClass);
            LOGGER.i(TAG, "type = " + type);
        }
        ApiBase<T> rtn = null;
        Gson gson = new GsonBuilder().create();
        if (type != null) {
            rtn = gson.fromJson(s, type);
        } else {
            rtn = gson.fromJson(s, ApiBase.class);
        }
        listener.onResponse(rtn);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    public ObjectRequest(final String url, final Response.Listener<ApiBase<T>> listener, Response.ErrorListener errorListener, final Class<T> tClass) {
        super(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                generateObject(s, url, tClass, listener);
            }
        }, errorListener);
    }
}
