package com.ymr.common.net.params;

import android.text.TextUtils;

import com.android.volley.Request;
import com.ymr.common.util.Constant;
import com.ymr.common.util.Tool;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ymr on 15/6/18.
 */
public abstract class SimpleNetParams implements NetRequestParams, Serializable {
    public static final long CURR_API_VERSION = 1;
    private String tailUrl;

    public SimpleNetParams(String tailUrl) {
        this.tailUrl = tailUrl;
    }

    public void setTailUrl(String tailUrl) {
        this.tailUrl = tailUrl;
    }

    @Override
    public int getMethod() {
        return getPostParams() == null ? Request.Method.GET : Request.Method.POST;
    }

    @Override
    public String getUrl() {
        Map<String, String> childGETParams = getChildGETParams();

        Map<String, String> sendMap = new HashMap<>();

        if (childGETParams != null && childGETParams.size() > 0) {
            sendMap.putAll(childGETParams);
        }

        sendMap.put(Constant.ServiceApi.API_VERSION, CURR_API_VERSION + "");
        sendMap.put(Constant.ServiceApi.OS,"android");
        return Tool.getUrl(getTailUrl(), sendMap);
    }

    /**
     * for method GET
     *
     * @return
     */
    protected abstract Map<String, String> getChildGETParams();

    @Override
    public Map<String, String> getPostParams() {
        return null;
    }

    public String getTailUrl() {
        return TextUtils.isEmpty(tailUrl) ? getChildTailUrl() : tailUrl;
    }

    protected String getChildTailUrl() {
        return null;
    }
}
