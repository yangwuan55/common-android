package com.ymr.appsearch.bean;

import com.ymr.appsearch.t9.ISeachItem;

/**
 * Created by ymr on 15/11/21.
 */
public class PhoneItem implements ISeachItem<PhoneItem> {
    @Override
    public String getPinYin() {
        return null;
    }

    @Override
    public String getShortPinYin() {
        return null;
    }

    @Override
    public int compareTo(PhoneItem another) {
        return 0;
    }
}
