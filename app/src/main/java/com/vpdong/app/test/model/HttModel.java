package com.vpdong.app.test.model;

import android.content.Context;
import com.google.library.android.mvp.MVPModel;
import com.google.library.android.rxlib.AndroidSchedulers;
import com.google.library.javase.model.HttpModelHelper;
import com.vpdong.app.test.data.JuheNews;
import com.vpdong.app.test.data.JuheResp;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import java.util.HashMap;
import java.util.Map;

public class HttModel extends MVPModel {
	private final String mKey = "d9ed0f2074df5443852c8791b3304c33";
	private final HttModelAPI mAPI;
	
	protected HttModel(Context context) {
		super(context);
		String host = "http://127.0.0.1";
		OkHttpClient.Builder clientBuilder = HttpModelHelper.buildClient();
		Retrofit.Builder retrofitBuilder = HttpModelHelper.buildRetrofit(host, clientBuilder.build());
		mAPI = retrofitBuilder.build().create(HttModelAPI.class);
	}
	
	public Observable<JuheNews> getNewsList(String type) {
		final String url = "http://v.juhe.cn/toutiao/index";
		final Map<String, String> params = new HashMap<>();
		params.put("type", type);
		params.put("key", mKey);
		return mAPI.getNewsList(HttpModelHelper.initUrl(url), params)
				.map(new Function<JuheResp<JuheNews>, JuheNews>() {
					@Override
					public JuheNews apply(JuheResp<JuheNews> resp) {
						if (resp == null || resp.getError_code() != 0) return null;
						return resp.getResult();
					}
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
	
	public interface HttModelAPI {
		@GET
		Observable<JuheResp<JuheNews>> getNewsList(@Url String url, @QueryMap Map<String, String> params);
	}
}
