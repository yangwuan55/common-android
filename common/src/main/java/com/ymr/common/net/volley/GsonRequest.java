/**
 * Copyright 2013 Mani Selvaraj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ymr.common.net.volley;

import com.android.volley.*;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom implementation of Request<T> class which converts the HttpResponse obtained to Java class objects.
 * Uses GSON library, to parse the response obtained.
 * Ref - JsonRequest<T>
 * @author Mani Selvaraj
 */

public class GsonRequest<T> extends Request<T>{

    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);
    private static final String TAG = "GsonRequest";

    private Listener<T> mListener;
    private Map<String, String> mParams;

    private Gson mGson;
    private Class<T> mJavaClass;

    /**
     * GET METHOD
     * @param url
     * @param cls
     * @param listener
     * @param errorListener
     */
    public GsonRequest(String url, Class<T> cls, Listener<T> listener,
                       ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        init(cls, listener);
    }

    /**
     * POST METHOD
     * @param url
     * @param cls
     * @param listener
     * @param errorListener
     * @param params
     */
    public GsonRequest(String url, Class<T> cls, Listener<T> listener,
                       ErrorListener errorListener,Map<String,String> params) {
        super(Method.POST,url,errorListener);
        init(cls,listener);
        mParams = params;
    }

    private void init(Class<T> cls, Listener<T> listener) {
        mGson = new Gson();
        mJavaClass = cls;
        mListener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    private Map<String, String> headers = new HashMap<String, String>();

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            T parsedGSON = mGson.fromJson(jsonString, mJavaClass);
            return Response.success(parsedGSON,
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}