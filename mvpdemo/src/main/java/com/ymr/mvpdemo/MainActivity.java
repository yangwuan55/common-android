package com.ymr.mvpdemo;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.ymr.mvpdemo.databinding.ActivityMainBinding;
import com.ymr.supermvp.android.databinding.activity.DataBindingActivity;

public class MainActivity extends DataBindingActivity<MainPresenter> implements MainView {

    private ActivityMainBinding mViewDataBinding;

    @Override
    public void finishCreateDataBinding(ViewDataBinding viewDataBinding, MainPresenter presenter) {
        mViewDataBinding = ((ActivityMainBinding) viewDataBinding);
        mViewDataBinding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().clickShow();
            }
        });
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public MainPresenter createPresenter(Context context) {
        return new MainPresenter();
    }

    @Override
    public boolean isHelloShowing() {
        return mViewDataBinding.hello.isShown();
    }

    @Override
    public void hideHello() {
        mViewDataBinding.hello.setVisibility(View.GONE);
    }

    @Override
    public void showHello() {
        mViewDataBinding.hello.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBtnText(String text) {
        mViewDataBinding.btnShow.setText(text);
    }
}
