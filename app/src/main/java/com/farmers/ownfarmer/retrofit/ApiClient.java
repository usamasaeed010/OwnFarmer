package com.farmers.ownfarmer.retrofit;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String BASE_URL = "http://192.168.0.119/ownfarmer/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Activity activity) {
        if (retrofit==null)
        {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            File httpCacheDirectory = new File(activity.getCacheDir(), "http-cache");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);

            OkHttpClient.Builder client = new OkHttpClient.Builder();

            client.connectTimeout(2, TimeUnit.MINUTES) // connect timeout
                    .writeTimeout(2, TimeUnit.MINUTES) // write timeout
                    .readTimeout(2, TimeUnit.MINUTES); // read timeout

            client.addNetworkInterceptor(new CacheInterceptor())
                    .cache(cache);
            client.addInterceptor(loggingInterceptor);
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
