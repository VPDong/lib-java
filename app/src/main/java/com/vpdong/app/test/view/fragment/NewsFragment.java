package com.vpdong.app.test.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.library.android.mvp.view.MVPFragment;
import com.google.library.android.view.RefreshLayout;
import com.vpdong.app.test.R;

import java.util.LinkedList;
import java.util.List;

public class NewsFragment extends MVPFragment<NewsPresenter> {
	private RvAdapter mAdapter;
	private RecyclerView mRecyclerView;
	private RefreshLayout mRefreshLayout;
	
	private List<String> mData = new LinkedList<>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_news);
	}
	
	@Override
	public void initView(View root) {
		mAdapter = new RvAdapter(getAppContext(), mData);
		mRecyclerView = root.findViewById(R.id.rv);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getAppContext()));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.addItemDecoration(new DividerItemDecoration(getAppContext(), DividerItemDecoration.VERTICAL));
		mRefreshLayout = root.findViewById(R.id.sr);
		mRefreshLayout.setOnPullListener(new RefreshLayout.OnPullListener() {
			@Override
			public void onPull() {
				mRefreshLayout.setPulling(true);
				mAdapter.getData().clear();
				for (int i = 0; i < 20; i++) {
					mAdapter.getData().add("add" + i);
				}
				getHandler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mRefreshLayout.setPulling(false);
					}
				}, 1000);
			}
		});
		mRefreshLayout.setOnPushListener(new RefreshLayout.OnPushListener() {
			@Override
			public void onPush() {
				// todo
			}
		});
	}
	
	@Override
	public View getTabView(Context context) {
		if (mTabView == null) {
			mTabView = LayoutInflater.from(context).inflate(R.layout.item_tab, null);
			ImageView iv = mTabView.findViewById(R.id.iv_tab);
			iv.setImageResource(R.drawable.icn_tab_news_selector);
			TextView tv = mTabView.findViewById(R.id.tv_tab);
			tv.setText("新闻");
		}
		return mTabView;
	}
	
	private class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvHolder> {
		private final Context mContext;
		private final LayoutInflater mInflater;
		private final List<String> mData;
		
		public RvAdapter(Context context, List<String> data) {
			this.mContext = context;
			this.mInflater = LayoutInflater.from(mContext);
			this.mData = data;
			for (int i = 0; i < 20; i++) {
				mData.add("item" + i);
			}
		}
		
		public List<String> getData() {
			return mData;
		}
		
		@Override
		public int getItemCount() {
			return mData.size();
		}
		
		@Override
		public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new RvHolder(mInflater.inflate(R.layout.item_news, parent, false));
		}
		
		@Override
		public void onBindViewHolder(RvHolder holder, int position) {
			holder.mTextView.setText(mData.get(position));
		}
		
		class RvHolder extends RecyclerView.ViewHolder {
			TextView mTextView;
			
			RvHolder(View item) {
				super(item);
				mTextView = item.findViewById(R.id.tv);
			}
		}
	}
}
