package com.vpdong.lib.java.android.rxlib;

import io.reactivex.Completable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import java.util.concurrent.CancellationException;

final class RxLifeFuncs {
    static final Function<RxLifeEvent, RxLifeEvent> COMP_FUNCTION = new Function<RxLifeEvent, RxLifeEvent>() {
        @Override
        public RxLifeEvent apply(RxLifeEvent lastEvent) throws Exception {
            switch (lastEvent) {
                case CREATE:
                    return RxLifeEvent.DESTROY;
                case START:
                    return RxLifeEvent.STOP;
                case RESUME:
                    return RxLifeEvent.PAUSE;
                case PAUSE:
                    return RxLifeEvent.STOP;
                case STOP:
                    return RxLifeEvent.DESTROY;
                case DESTROY:
                    throw new Error("Cannot bind to component life when out of it.");
                default:
                    throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
            }
        }
    };

    static final Function<Throwable, Boolean> RESUME_FUNCTION = new Function<Throwable, Boolean>() {
        @Override
        public Boolean apply(Throwable throwable) throws Exception {
            Exceptions.propagate(throwable);
            return false;
        }
    };

    static final Predicate<Boolean> SHOULD_COMPLETE = new Predicate<Boolean>() {
        @Override
        public boolean test(Boolean shouldComplete) throws Exception {
            return shouldComplete;
        }
    };

    static final Function<Object, Completable> CANCEL_COMPLETABLE = new Function<Object, Completable>() {
        @Override
        public Completable apply(Object ignore) throws Exception {
            return Completable.error(new CancellationException());
        }
    };

    private RxLifeFuncs() {
        throw new AssertionError("No instances!");
    }
}
