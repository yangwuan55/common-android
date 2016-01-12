package com.ymr.appsearch.t9;

import java.util.List;

/**
 * Created by ymr on 15/11/21.
 */
public interface IT9Search<S extends ISeachItem> {

    void setAllItems(List<S> allItems);

    List<S> search(String t9num);

}
