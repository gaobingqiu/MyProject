package com.gbq.myproject.base.usecase;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UseCaseThreadPoolScheduler implements UseCaseScheduler {
    private static final int MAX_POOL_SIZE = 3;
    private static final int POOL_SIZE = 2;
    private static final int TIMEOUT = 30;
    private final Handler mHandler = new Handler();
    private ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(POOL_SIZE), new MyThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());

    public void execute(Runnable runnable) {
        this.mThreadPoolExecutor.execute(runnable);
    }

    public <V extends UseCase.ResponseValue> void notifyResponse(final V result, final UseCase.UseCaseCallback<V> useCaseCallback) {
        this.mHandler.post(() -> useCaseCallback.onSuccess(result));
    }

    public <V extends UseCase.ResponseValue> void onError(final Integer code, final UseCase.UseCaseCallback<V> useCaseCallback) {
        this.mHandler.post(() -> useCaseCallback.onError(code));
    }
}
