package com.ymr.common.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ymr.common.ui.BaseUI;

/**
 * Created by ymr on 15/10/13.
 */
public interface BaseFragmentUI extends BaseUI {
    View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
