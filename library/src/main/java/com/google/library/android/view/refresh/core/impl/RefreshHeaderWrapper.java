package com.google.library.android.view.refresh.core.impl;

import android.annotation.SuppressLint;
import android.view.View;
import com.google.library.android.view.refresh.core.api.RefreshHeader;
import com.google.library.android.view.refresh.core.internal.InternalAbstract;

/**
 * 刷新头部包装
 * Created by scwang on 2017/5/26.
 */
@SuppressLint("ViewConstructor")
public class RefreshHeaderWrapper extends InternalAbstract implements RefreshHeader/*, InvocationHandler*/ {
    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }
}
