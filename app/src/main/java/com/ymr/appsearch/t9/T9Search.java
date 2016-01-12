package com.ymr.appsearch.t9;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ymr on 15/11/21.
 */
public class T9Search<S extends ISeachItem> implements IT9Search<S> {

    private List<S> mAllItems;
    private String mPreNumber;
    private List<S> mPreResult = new ArrayList<>();

    @Override
    public void setAllItems(List<S> allItems) {
        this.mAllItems = allItems;
    }

    @Override
    public List<S> search(String t9num) {
        List<S> result = new ArrayList<>();
        boolean newQuery = TextUtils.isEmpty(mPreNumber) || t9num.length() <= mPreNumber.length();
        for (S item : (newQuery?mAllItems:mPreResult)) {
            int pos = item.getPinYin().indexOf(t9num);
            if (pos != -1) {
                result.add(item);
                continue;
            }
            int posShort = item.getShortPinYin().indexOf(t9num);
            if (posShort != -1) {
                result.add(item);
            }
        }
        mPreResult.clear();
        if (!result.isEmpty()) {
            mPreResult.addAll(result);
            mPreNumber = t9num;
        } else {
            mPreNumber = "";
        }

        Collections.sort(result);
        return result;
    }
}
