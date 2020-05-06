package com.vpdong.lib.java.android.rxlib;

import android.os.Handler;
import android.os.Looper;
import io.reactivex.Scheduler;

public final class AndroidSchedulers {
    private static final Scheduler SCHEDULER = new AndroidScheduler(new Handler(Looper.getMainLooper()), false);

    public static Scheduler mainThread() {
        return SCHEDULER;
    }

    public static void verifyMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            String threadName = Thread.currentThread().getName();
            throw new IllegalStateException("Expected to be called on the main thread but was " + threadName);
        }
    }

    private AndroidSchedulers() {
        throw new AssertionError("No instances.");
    }
}
