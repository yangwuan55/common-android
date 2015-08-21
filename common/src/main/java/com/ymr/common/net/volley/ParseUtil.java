package com.ymr.common.net.volley;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ymr.common.bean.ApiBase;
import com.ymr.common.util.LOGGER;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by ymr on 15/8/21.
 */
public class ParseUtil {
    private static final String TAG = "ParseUtil";

    public static <T> void generateObject(String response, String url, Class<T> tClass, Response.Listener<ApiBase<T>> listener) {
        LOGGER.i(TAG, "URL = " + url);
        ParameterizedType type = null;
        if (tClass != null) {
            type = type(ApiBase.class, tClass);
            LOGGER.i(TAG, "type = " + type);
        }
        ApiBase<T> rtn = null;
        Gson gson = new GsonBuilder().create();
        if (type != null) {
            rtn = gson.fromJson(response, type);
        } else {
            rtn = gson.fromJson(response, ApiBase.class);
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
}
