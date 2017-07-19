package com.zhang.rxbustest.rxbus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class RxDisposables {
    private static CompositeDisposable mDisposable = new CompositeDisposable();

    public static void add(Disposable d) {
        if (d != null) {
            mDisposable.add(d);
        }
    }

    public static void remove(Disposable d) {
        if (d != null) {
            mDisposable.remove(d);
        }
    }

    public static void clear() {
        mDisposable.clear();
    }

    public static void dispose() {
        mDisposable.dispose();
    }

    public static int hasDisposables() {
        return mDisposable.hashCode();
    }
}
