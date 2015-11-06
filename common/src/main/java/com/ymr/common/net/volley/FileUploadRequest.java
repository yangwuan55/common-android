package com.ymr.common.net.volley;

import com.android.volley.Response;
import com.ymr.common.bean.IApiBase;

/**
 * Created by ymr on 15/8/21.
 */
public class FileUploadRequest<D> extends MultiPartStringRequest {
    private static final String TAG = "FileUploadRequest";

    /**
     * Creates a new request with the given method.
     *
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public FileUploadRequest(final String url, final Response.Listener<IApiBase<D>> listener, final Response.ErrorListener errorListener, final Class<D> tClass) {
        super(Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ParseUtil.generateObject(response, url, tClass, listener,errorListener);
            }
        }, errorListener);
    }
}
