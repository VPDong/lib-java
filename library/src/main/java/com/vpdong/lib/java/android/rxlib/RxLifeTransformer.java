package com.vpdong.lib.java.android.rxlib;

import io.reactivex.*;
import org.reactivestreams.Publisher;

public final class RxLifeTransformer<T> implements ObservableTransformer<T, T>,
        FlowableTransformer<T, T>, SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer {
    private final Observable<?> mObservable;

    RxLifeTransformer(Observable<?> observable) {
        mObservable = observable;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.takeUntil(mObservable);
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.takeUntil(mObservable.toFlowable(BackpressureStrategy.LATEST));
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.takeUntil(mObservable.firstOrError());
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.takeUntil(mObservable.firstElement());
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return Completable.ambArray(upstream, mObservable.flatMapCompletable(RxLifeFuncs.CANCEL_COMPLETABLE));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        RxLifeTransformer<?> that = (RxLifeTransformer<?>) object;
        return mObservable.equals(that.mObservable);
    }

    @Override
    public int hashCode() {
        return mObservable.hashCode();
    }

    @Override
    public String toString() {
        return "RxLifeTransformer{observable=" + mObservable + '}';
    }
}
