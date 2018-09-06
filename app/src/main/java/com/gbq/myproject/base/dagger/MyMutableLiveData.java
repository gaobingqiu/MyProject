package com.gbq.myproject.base.dagger;

import android.arch.lifecycle.MutableLiveData;

import javax.inject.Inject;

public class MyMutableLiveData<T> extends MutableLiveData<T> {
    @SuppressWarnings("WeakerAccess")
    @Inject
    public MyMutableLiveData(){ }
}
