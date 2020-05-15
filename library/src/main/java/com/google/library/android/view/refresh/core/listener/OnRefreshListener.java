package com.google.library.android.view.refresh.core.listener;

import androidx.annotation.NonNull;
import com.google.library.android.view.refresh.core.api.RefreshLayout;

/**
 * 刷新监听器
 * Created by scwang on 2017/5/26.
 */
public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
