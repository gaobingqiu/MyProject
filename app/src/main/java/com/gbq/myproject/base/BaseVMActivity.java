package com.gbq.myproject.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gbq.myproject.util.LogUtil;

import java.lang.reflect.ParameterizedType;

public abstract class BaseVMActivity<T extends BaseVm> extends AppCompatActivity {

    protected T mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.i(getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        initVm();
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(getClass().getSimpleName(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(getClass().getSimpleName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(getClass().getSimpleName(), "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(getClass().getSimpleName(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(getClass().getSimpleName(), "onDestroy");
    }

    protected abstract int getContentId();

    //使用了泛型参数化
    private void initVm() {
        try {
            ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
            // noinspection unchecked
            Class<T> clazz = (Class<T>) pt.getActualTypeArguments()[0];
            mViewModel = ViewModelProviders.of(this).get(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Lifecycle lifecycle = getLifecycle();
        lifecycle.addObserver(mViewModel);
    }

    protected abstract void initView();

    protected abstract void initData();
}
