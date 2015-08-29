package com.ymr.mvp.params;

import com.ymr.common.net.params.SimpleNetParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ymr on 15/8/20.
 */
public abstract class ListParams extends SimpleNetParams {

    private int page;
    private int pagesize;

    public ListParams(String tailUrl) {
        super(tailUrl);
    }

    public void setPageParam(int page,int pagesize) {
        this.page = page;
        this.pagesize = pagesize;
    }

    /**
     * page and pagesize are the params of GET method.
     * @return
     */
    @Override
    protected Map<String, String> getChildGETParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("pagesize", pagesize + "");
        Map<String, String> otherParams = getOtherParams();
        if (otherParams != null && !otherParams.isEmpty()) {
            map.putAll(otherParams);
        }
        return map;
    }

    protected abstract Map<String,String> getOtherParams();
}
