package com.ymr.common.bean;

import java.io.Serializable;

/**
 * Created by bigpeach on 15/5/7.
 */

public class Action<T extends Serializable> implements Serializable {
    public String actiontype;
    public T extparam;
    public String pagetype;
    public String title;
    public String url;

    public String localaction;

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public String getPagetype() {
        return pagetype;
    }

    public void setPagetype(String pagetype) {
        this.pagetype = pagetype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getExtparam() {
        return extparam;
    }

    public void setExtparam(T extparam) {
        this.extparam = extparam;
    }

    public String getLocalaction() {
        return localaction;
    }

    public void setLocalaction(String localaction) {
        this.localaction = localaction;
    }

    @Override
    public String toString() {
        return "Action{" +
                "actiontype='" + actiontype + '\'' +
                ", extparam='" + extparam + '\'' +
                ", pagetype='" + pagetype + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
