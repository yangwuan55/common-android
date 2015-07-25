package com.ymr.common.net.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ymr.common.net.params.NetRequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by ymr on 15/5/7.
 */
public class VolleyUtil {
    //最大缓存50mb
    public static final long MAX_DISK_SIZE = 8 * 1024 * 1024 * 50;
    private static final String TAG = "VolleyUtil";
    private static VolleyUtil sInstance;
    private final Context mContext;
    private final RequestQueue mRequestQueue;
    private final DisplayImageOptions mImageOptions;
    private ImageLoader mImageLoader;

    public interface RequestListner<T> {
        void onSuccess(T t);
        void onFail(VolleyError error);
    }
    public interface ImageLoadSuccessLisener{
        void onLoadSuccess(Bitmap bitmap);
        void onFail();
    }
    private VolleyUtil(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
        mImageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        mImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public static VolleyUtil getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VolleyUtil(context);
        }
        return sInstance;
    }

    public void addRequest(Request request) {
        mRequestQueue.add(request);
    }

    public void addJsonObjectRequest(String url,Map<String,String> params, final RequestListner<JSONObject> requestListner) {
        final String getUrl = getUrl(url, params);
        mRequestQueue.add(new JsonObjectRequest(Request.Method.GET, getUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                requestListner.onSuccess(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                requestListner.onFail(volleyError);
            }
        }));
    }

    public void addJsonArrayRequest(String url,Map<String,String> params, final RequestListner<JSONArray> requestListner) {
        final String getUrl = getUrl(url, params);
        mRequestQueue.add(new JsonArrayRequest(Request.Method.GET, getUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                requestListner.onSuccess(jsonArray);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                requestListner.onFail(volleyError);
            }
        }));
    }

    public <T> void addGetGsonRequest(String url, Class<T> tClass, Map<String, String> params, final RequestListner<T> requestListner) {
        String getUrl = getUrl(url, params);
        GsonRequest<T> gsonRequest = new GsonRequest<T>(getUrl, tClass, new Response.Listener<T>() {
            @Override
            public void onResponse(T t) {
                requestListner.onSuccess(t);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                requestListner.onFail(volleyError);
            }
        });
        mRequestQueue.add(gsonRequest);
    }

    public <T> void addPostGsonRequest(String url, Class<T> tClass, Map<String, String> params, final RequestListner<T> requestListner) {

        new GsonRequest<T>(url, tClass, new Response.Listener<T>() {
            @Override
            public void onResponse(T t) {
                requestListner.onSuccess(t);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                requestListner.onFail(volleyError);
            }
        }, params);
    }

    public void loadImage(String url, ImageView view, int defultRes, int errorRes) {
        mImageLoader.displayImage(url, view, new DisplayImageOptions.Builder()
                .showImageOnLoading(defultRes)
                .showImageOnFail(errorRes)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build());
    }

    public void loadImage(String url,final ImageLoadSuccessLisener listener){
        mImageLoader.loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                listener.onFail();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                listener.onLoadSuccess(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                listener.onFail();
            }
        });
    }

    public void loadImage(String url, ImageView imageView) {
        mImageLoader.displayImage(url, imageView, mImageOptions);
    }

    public static String getUrl(String url, Map<String, String> params) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        if (params != null) {
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = params.get(key);
                builder.appendQueryParameter(key,value);
            }
        }
        return builder.toString();
    }

    public <T,P extends NetRequestParams> void addRequest(final P params,Class<T> tClass,final RequestListner<T> requestListner) {
        ObjectRequest<T> gsonRequest = null;

        final Response.Listener<T> listener = new Response.Listener<T>() {
            @Override
            public void onResponse(T t) {
                requestListner.onSuccess(t);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                requestListner.onFail(volleyError);
            }
        };

        switch (params.getMethod()) {
            case Request.Method.GET:
                gsonRequest = new ObjectRequest<T>(Request.Method.GET, params.getUrl(),
                        new Response.Listener<T>() {
                            @Override
                            public void onResponse(T response) {
                                requestListner.onSuccess(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestListner.onFail(error);
                    }
                },tClass);
                break;

            case Request.Method.POST:
                //gsonRequest = new GsonRequest<>(params.getUrl(),tClass,listener,errorListener,params.getPostParams());
                gsonRequest = new ObjectRequest<T>(Request.Method.POST, params.getUrl(),
                        new Response.Listener<T>() {
                            @Override
                            public void onResponse(T response) {
                                requestListner.onSuccess(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestListner.onFail(error);
                    }
                },tClass) {
                    @Override
                    protected Map<String, String> getParams() {
                        return params.getPostParams();
                    }
                };
                break;
        }
        addRequest(gsonRequest);
    }
}
