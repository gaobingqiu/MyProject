package com.gbq.myproject.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbq.myproject.util.LogUtil;

import java.lang.reflect.ParameterizedType;

public abstract class BaseVMFragment<T extends BaseVm> extends Fragment {

    protected T mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.i(getClass().getSimpleName(), "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(getClass().getSimpleName() + "onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LogUtil.i(getClass().getSimpleName(), "onCreateView");
        View view = inflater.inflate(getContentId(), container, false);
        initVm();
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d(getClass().getSimpleName() + "onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d(getClass().getSimpleName(), "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(getClass().getSimpleName(), "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d(getClass().getSimpleName(), "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d(getClass().getSimpleName(), "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(getClass().getSimpleName(), "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d(getClass().getSimpleName() + "onDetach()");
    }

    protected abstract int getContentId();

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

    protected abstract void initView(View view);

    protected abstract void initData();
}
