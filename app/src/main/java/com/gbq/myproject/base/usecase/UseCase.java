package com.gbq.myproject.base.usecase;

public abstract class UseCase<Q extends UseCase.RequestValues, P extends UseCase.ResponseValue> {
    public final static int CODE = -6;
    private Q mRequestValues;
    private UseCaseCallback<P> mUseCaseCallback;

    protected abstract void executeUseCase(Q value);

    public Q getRequestValues() {
        return this.mRequestValues;
    }

    public UseCaseCallback<P> getUseCaseCallback() {
        return this.mUseCaseCallback;
    }

    void run() {
        executeUseCase(this.mRequestValues);
    }

    public void setRequestValues(Q value) {
        this.mRequestValues = value;
    }

    public void setUseCaseCallback(UseCaseCallback<P> useCaseCallback) {
        this.mUseCaseCallback = useCaseCallback;
    }

    public interface RequestValues {
    }

    public interface ResponseValue {
    }

    public interface UseCaseCallback<R> {
        void onError(Integer code);

        void onSuccess(R result);
    }
}
