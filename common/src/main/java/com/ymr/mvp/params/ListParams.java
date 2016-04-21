package com.ymr.mvp.params;

import android.support.annotation.NonNull;

import com.ymr.common.net.params.DomainUrl;
import com.ymr.common.net.params.SimpleNetParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ymr on 15/8/20.
 */
public abstract class ListParams extends SimpleNetParams {

    public static final int DEFAULT_START_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 30;
    private int page;
    private int pagesize = DEFAULT_PAGE_SIZE;
    private int startPage = DEFAULT_START_PAGE;

    private boolean isGetedPageSize = false;
    private boolean isGetedStartPage = false;

    public ListParams(String tailUrl) {
        super(tailUrl);
        initListParams();
    }

    public ListParams(String tailUrl, DomainUrl domainUrl) {
        super(tailUrl, domainUrl);
        initListParams();
    }

    private void initListParams() {
        if (!isGetedStartPage) {
            isGetedStartPage = true;
            this.startPage = createStartPage();
        }
        if (!isGetedPageSize) {
            isGetedPageSize = true;
            int pageSize = createPageSize();
            if (pagesize != 0) {
                this.pagesize = pageSize;
            }
        }
    }

    public void setCurrPage(int page) {
        this.page = page;
    }

    /**
     * page and pagesize are the params of GET method.
     * @return
     */
    @Override
    protected Map<String, String> getChildGETParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put(getPageParamsName(), page + "");
        map.put(getPageSizeName(), pagesize + "");
        Map<String, String> otherParams = getOtherParams();
        if (otherParams != null && !otherParams.isEmpty()) {
            map.putAll(otherParams);
        }
        return map;
    }

    @NonNull
    protected String getPageSizeName() {
        return "pagesize";
    }

    @NonNull
    protected String getPageParamsName() {
        return "page";
    }

    protected abstract Map<String,String> getOtherParams();

    public int getPage() {
        return page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public int getStartPage() {
        return startPage;
    }

    protected abstract int createPageSize();

    protected abstract int createStartPage();
}
