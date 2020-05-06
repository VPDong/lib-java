package com.vpdong.lib.java.android.rxlib;

import io.reactivex.Observable;

public interface RxLifeProvider<E> {
    Observable<E> life();

    /**
     * Binds a source until the next reasonable event occurs.
     *
     * @return a reusable {@link RxLifeTransformer} which unSubscribes at the correct time.
     */
    <T> RxLifeTransformer<T> bindToLife();

    /**
     * Binds a source until a specific event occurs.
     *
     * @param event the event that triggers unSubscription
     * @return a reusable {@link RxLifeTransformer} which unSubscribes when the event triggers.
     */
    <T> RxLifeTransformer<T> bindUntilEvent(E event);
}
