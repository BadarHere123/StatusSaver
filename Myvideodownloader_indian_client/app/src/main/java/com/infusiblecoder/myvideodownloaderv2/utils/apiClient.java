package com.infusiblecoder.myvideodownloaderv2.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class apiClient {
    private static final String CACHE_CONTROL = "Cache-Control";
    private static Retrofit retrofit;

    public static Retrofit initClient() {
        return new Builder().baseUrl(Data.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            new HttpLoggingInterceptor();
            retrofit = new Builder().baseUrl(Data.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            public void log(String str) {
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        return httpLoggingInterceptor;
    }

    public static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            public Response intercept(Chain chain) throws IOException {
                Response proceed = chain.proceed(chain.request());
                CacheControl build = new CacheControl.Builder().maxAge(2, TimeUnit.SECONDS).build();
                return proceed.newBuilder().header(apiClient.CACHE_CONTROL, build.toString()).build();
            }
        };
    }
}
