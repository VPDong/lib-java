package com.vpdong.lib.java.android.rxlib;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class RxLife {
    protected RxLife() {
        // nothing
    }

    protected static <T, R> RxLifeTransformer<T> bindFunc(final Observable<R> life) {
        return new RxLifeTransformer<>(life);
    }

    public static <T, R> RxLifeTransformer<T> bind(final Observable<R> life, final Function<R, R> events) {
        Observable<Boolean> observable = Observable.combineLatest(
                life.take(1).map(events), life.skip(1),
                new BiFunction<R, R, Boolean>() {
                    @Override
                    public Boolean apply(R bindUntilEvent, R lifecycleEvent) throws Exception {
                        return lifecycleEvent.equals(bindUntilEvent);
                    }
                })
                .onErrorReturn(RxLifeFuncs.RESUME_FUNCTION)
                .filter(RxLifeFuncs.SHOULD_COMPLETE);
        return bindFunc(observable);
    }

    public static <T> RxLifeTransformer<T> bind(final Observable<RxLifeEvent> life) {
        return bind(life, RxLifeFuncs.COMP_FUNCTION);
    }

    public static <T, R> RxLifeTransformer<T> bindUntilEvent(final Observable<R> life, final R event) {
        Observable<R> observable = life.filter(new Predicate<R>() {
            @Override
            public boolean test(R lifeEvent) throws Exception {
                return lifeEvent.equals(event);
            }
        });
        return bindFunc(observable);
    }
}
