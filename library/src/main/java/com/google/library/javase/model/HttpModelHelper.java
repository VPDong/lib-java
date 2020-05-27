package com.google.library.javase.model;

import com.google.library.javase.utils.FileUtils;
import com.google.library.javase.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.*;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class HttpModelHelper {
    /**
     * 获取OkHttpClient的实例
     *
     * @return OkHttpClient的配置实例
     */
    public static OkHttpClient.Builder buildClient() {
        return new OkHttpClient.Builder()
                // 错误重连
                .retryOnConnectionFailure(true)
                // 连接的超时时间
                .connectTimeout(3 * 1000, TimeUnit.MILLISECONDS)
                // 读操作的超时时间
                .readTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                // 写操作的超时时间
                .writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取Retrofit的实例
     *
     * @param url    基础URL,不建议以/结尾
     * @param client OkHttpClient的实例
     * @return Retrofit的配置实例
     */
    public static Retrofit.Builder buildRetrofit(String url, OkHttpClient client) {
        if (url == null || url.trim().equals("")) throw new IllegalArgumentException("url can not be null");
        url = url.startsWith("http") ? url : String.format("http://%s", url.trim());
        if (client == null) throw new IllegalArgumentException("client can not be null");
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 配置rxjava
                .addConverterFactory(ScalarsConverterFactory.create())// 配置string
                .addConverterFactory(GsonConverterFactory.create())// 配置json
                .callbackExecutor(Executors.newSingleThreadExecutor())// 一般网络请求不设置可能不会报错,但如果是下载文件就会报错
                .client(client)
                .baseUrl(url);
    }

    public static String initUrl(String url) {
        if (url == null) throw new Error("url can not be null");
        url = url.startsWith("http") ? url.trim() : String.format("http://%s", url.trim());
        if (Pattern.matches("(https?)://[a-zA-Za-z0-9&@#/%+\\-?=~_|!:,.;]+[a-zA-Z0-9&@#/%+\\-=~_|]", url)) {
            return url;
        } else {
            throw new Error("url is error");
        }
    }

    public static class HttpModelCom {
        private static volatile HttpModelCom INSTANCE;

        public static HttpModelCom getInstance() {
            if (INSTANCE == null) {
                synchronized (HttpModelCom.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new HttpModelCom();
                    }
                }
            }
            return INSTANCE;
        }

        private final HttpModelComAPI mAPI;

        private HttpModelCom() {
            String host = "http://127.0.0.1";
            OkHttpClient.Builder clientBuilder = HttpModelHelper.buildClient();
            // todo 设置clientBuilder其他的属性
            Retrofit.Builder retrofitBuilder = HttpModelHelper.buildRetrofit(host, clientBuilder.build());
            // todo 设置retrofitBuilder其他的属性
            mAPI = retrofitBuilder.build().create(HttpModelComAPI.class);
        }

        public <T> T doGetJson(String url, Map<String, String> params, Type type) {
            if (url == null || type == null) return null;
            try {
                params = params == null ? new HashMap<String, String>() : params;
                String resp = mAPI.doGetJson(initUrl(url), params).execute().body();
                return JsonUtils.fromJson(resp, type);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public boolean doGetFile(String url, Map<String, String> params, File downDir) {
            if (url == null) return false;
            ResponseBody response = null;
            try {
                response = mAPI.doGetFile(initUrl(url), params).execute().body();
                if (response == null) return false;

                FileUtils.toDelete(downDir, true);
                return FileUtils.setContent(downDir, response.byteStream(), false);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                FileUtils.close(response);
            }
        }

        public <T> T doPostJson(String url, Object body, Type type) {
            if (url == null || type == null) return null;
            try {
                String resp = mAPI.doPostJson(initUrl(url), body).execute().body();
                return JsonUtils.fromJson(resp, type);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public <T> T doPostForm(String url, Map<String, Object> body, Type type) {
            if (url == null || type == null) return null;
            try {
                String resp = mAPI.doPostForm(initUrl(url), body).execute().body();
                return JsonUtils.fromJson(resp, type);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 一个简单的接口配置
         */
        public interface HttpModelComAPI {
            @GET
            Call<String> doGetJson(@Url String url, @QueryMap Map<String, String> params);

            @GET
            @Streaming
            Call<ResponseBody> doGetFile(@Url String url, @QueryMap Map<String, String> params);

            @POST
            Call<String> doPostJson(@Url String url, @Body Object body);

            @POST
            @FormUrlEncoded
            Call<String> doPostForm(@Url String url, @FieldMap Map<String, Object> body);
        }
    }
}
