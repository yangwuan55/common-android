package com.ymr.common.net.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;
import com.ymr.common.bean.ApiBase;
import com.ymr.common.net.params.FileParams;
import com.ymr.common.net.params.NetRequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ymr on 15/5/7.
 */
public class VolleyUtil {
    //最大缓存50mb
    public static final long MAX_DISK_SIZE = 8 * 1024 * 1024 * 50;
    private static final String TAG = "VolleyUtil";
    public static final String DEFAULT_TAG = "default_tag";
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
        mRequestQueue = Volley.newRequestQueue(mContext, new MultiPartStack());
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

    public void cancelAll() {
        mRequestQueue.cancelAll(DEFAULT_TAG);
    }

    public void addRequest(Request request) {
        request.setTag(DEFAULT_TAG);
        mRequestQueue.add(request);
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

    public <T,P extends NetRequestParams> void addRequest(final P params,final RequestListner<ApiBase<T>> requestListner,Class<T> tClass) {
        Request request = null;
        if (params instanceof FileParams) {
            request = getFileUploadRequest(((FileParams) params), requestListner, tClass);
        } else {
            request = getNormalObjectRequest(params, requestListner, tClass);
        }
        addRequest(request);
    }

    @Nullable
    private <T, P extends NetRequestParams> ObjectRequest<T> getNormalObjectRequest(final P params, final RequestListner<ApiBase<T>> requestListner, final Class<T> tClass) {
        ObjectRequest<T> gsonRequest = null;

        final Response.Listener<ApiBase<T>> listener = new Response.Listener<ApiBase<T>>() {
            @Override
            public void onResponse(ApiBase<T> response) {
                requestListner.onSuccess(response);
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
                        listener,errorListener,tClass);
                break;

            case Request.Method.POST:
                gsonRequest = new ObjectRequest<T>(Request.Method.POST, params.getUrl(),
                        listener,errorListener,tClass) {
                    @Override
                    protected Map<String, String> getParams() {
                        return params.getPostParams();
                    }
                };
                break;
        }
        return gsonRequest;
    }

    @NonNull
    private <T, P extends FileParams> FileUploadRequest<T> getFileUploadRequest(final P params, final RequestListner<ApiBase<T>> requestListner, final Class<T> tClass) {
        FileUploadRequest<T> fileUploadRequest = null;

        final Response.Listener<ApiBase<T>> listener = new Response.Listener<ApiBase<T>>() {
            @Override
            public void onResponse(ApiBase<T> response) {
                requestListner.onSuccess(response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                requestListner.onFail(volleyError);
            }
        };

        fileUploadRequest = new FileUploadRequest(params.getUrl(),listener, errorListener, tClass) {
            @Override
            protected Map<String, String> getParams() {
                return params.getPostParams();
            }
        };
        fileUploadRequest.addFileUpload(params.getFileMap());

        fileUploadRequest.setRetryPolicy(//关闭retry
                new DefaultRetryPolicy(
                        Integer.MAX_VALUE,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return fileUploadRequest;
    }
}
