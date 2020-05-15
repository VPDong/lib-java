package com.google.library.android.view.refresh.core.listener;


import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import com.google.library.android.view.refresh.core.api.RefreshLayout;
import com.google.library.android.view.refresh.core.constant.RefreshState;

import static androidx.annotation.RestrictTo.Scope.*;

/**
 * 刷新状态改变监听器
 * Created by scwang on 2017/5/26.
 */
public interface OnStateChangedListener {
    /**
     * 【仅限框架内调用】状态改变事件 {@link RefreshState}
     * @param refreshLayout RefreshLayout
     * @param oldState 改变之前的状态
     * @param newState 改变之后的状态
     */
    @RestrictTo({LIBRARY,LIBRARY_GROUP,SUBCLASSES})
    void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState);
}
