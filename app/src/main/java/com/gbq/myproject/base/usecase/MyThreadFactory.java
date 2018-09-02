package com.gbq.myproject.base.usecase;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory {
    private static final String NAME = "pool-accounts-";
    private int counter = 0;

    public Thread newThread(@NonNull Runnable runnable) {
        Thread thread= new Thread(runnable, NAME+"Thread-" + this.counter);
        this.counter += 1;
        return thread;
    }
}