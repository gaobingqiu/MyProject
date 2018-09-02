package com.gbq.myproject.base.usecase;

public interface UseCaseScheduler {
    void execute(Runnable runnable);

    <V extends UseCase.ResponseValue> void notifyResponse(V result, UseCase.UseCaseCallback<V> useCaseCallback);

    <V extends UseCase.ResponseValue> void onError(Integer code, UseCase.UseCaseCallback<V> useCaseCallback);
}

