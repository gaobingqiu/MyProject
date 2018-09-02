package com.gbq.myproject.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.NonNull;

public abstract class BaseVm extends AndroidViewModel implements LifecycleObserver {

    public BaseVm(@NonNull Application application) {
        super(application);
    }
}
