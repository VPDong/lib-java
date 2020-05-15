package com.google.library.android.view.refresh.core.impl;

import android.annotation.SuppressLint;
import android.view.View;
import com.google.library.android.view.refresh.core.api.RefreshFooter;
import com.google.library.android.view.refresh.core.internal.InternalAbstract;

/**
 * 刷新底部包装
 * Created by scwang on 2017/5/26.
 */
@SuppressLint("ViewConstructor")
public class RefreshFooterWrapper extends InternalAbstract implements RefreshFooter/*, InvocationHandler */{
    public RefreshFooterWrapper(View wrapper) {
        super(wrapper);
    }
}
