package com.gbq.myproject.base.usecase;

import com.gbq.myproject.util.LogUtil;

@SuppressWarnings("unused")
public class UseCaseHandler {
    private static UseCaseHandler INSTANCE;
    private final UseCaseScheduler mUseCaseScheduler;

    private UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        mUseCaseScheduler = useCaseScheduler;
    }

    public static UseCaseHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
        }
        return INSTANCE;
    }

    private <V extends UseCase.ResponseValue> void notifyError(Integer code, UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.onError(code, useCaseCallback);
    }

    //only call on UI thread
    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void execute(final UseCase<T, R> task, T value, UseCase.UseCaseCallback<R> useCaseCallback) {
        task.setRequestValues(value);
        task.setUseCaseCallback(new UiCallbackWrapper<>(useCaseCallback, this));
        mUseCaseScheduler.execute(task::run);
    }

    //can call at any thread,result is on a async thread
    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void executeDirectly(final UseCase<T, R> task, T value, UseCase.UseCaseCallback<R> useCaseCallback) {
        task.setRequestValues(value);
        task.setUseCaseCallback(new DirectlyCallbackWrapper<>(useCaseCallback, this));
        mUseCaseScheduler.execute(task::run);
    }

    //can call at any thread,result is on a async thread
    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void executeDirectly(final UseCase<T, R> task, UseCase.UseCaseCallback<R> useCaseCallback) {
        executeDirectly(task, null, useCaseCallback);
    }

    //can call at any thread,result is on a async thread
    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void executeDirectly(final UseCase<T, R> task) {
        executeDirectly(task, null, null);
    }

    public <V extends UseCase.ResponseValue> void notifyResponse(V code, UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.notifyResponse(code, useCaseCallback);
    }

    private static class UiCallbackWrapper<V extends UseCase.ResponseValue>
            implements UseCase.UseCaseCallback<V> {
        protected final UseCase.UseCaseCallback<V> mCallback;
        protected final UseCaseHandler mUseCaseHandler;

        public UiCallbackWrapper(UseCase.UseCaseCallback<V> useCaseCallback, UseCaseHandler useCaseHandler) {
            mCallback = useCaseCallback;
            mUseCaseHandler = useCaseHandler;
        }

        @Override
        public void onError(Integer code) {
            if(mCallback == null){
                LogUtil.e("mCallback is null,please set your callback");
                return;
            }
            mUseCaseHandler.notifyError(code, mCallback);
        }

        @Override
        public void onSuccess(V result) {
            if(mCallback == null){
                LogUtil.e("mCallback is null,please set your callback");
                return;
            }
            mUseCaseHandler.notifyResponse(result, mCallback);
        }
    }

    private static final class DirectlyCallbackWrapper<V extends UseCase.ResponseValue>
            extends UiCallbackWrapper<V> {

        public DirectlyCallbackWrapper(UseCase.UseCaseCallback<V> useCaseCallback, UseCaseHandler useCaseHandler) {
            super(useCaseCallback, useCaseHandler);
        }

        @Override
        public void onError(Integer code) {
            if(mCallback == null){
                LogUtil.e("mCallback is null,please set your callback");
                return;
            }
            mCallback.onError(code);
        }

        @Override
        public void onSuccess(V result) {
            if(mCallback == null){
                LogUtil.e("mCallback is null,please set your callback");
                return;
            }
            mCallback.onSuccess(result);
        }
    }
}