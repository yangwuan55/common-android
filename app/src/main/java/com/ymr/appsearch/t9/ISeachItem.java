package com.ymr.appsearch.t9;

/**
 * Created by ymr on 15/11/21.
 */
public interface ISeachItem<T extends ISeachItem> extends Comparable<T>{

    String getPinYin();

    String getShortPinYin();

}
