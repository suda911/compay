package com.example.nabsterz0r.compay.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.nabsterz0r.compay.ApiService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    private String apikey;
    private String server;

    public NetModule(String server ,String akey) {
        this.apikey = akey;
        this.server = server;
    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .disableHtmlEscaping();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, Application application) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(logging);
        //убрать - это для теста
        clientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            HttpUrl origUrl = request.url();

            HttpUrl url = origUrl.newBuilder().addQueryParameter("appid", apikey).build();

            Request.Builder builder = request.newBuilder().url(url);
            Request r = builder.build();

            return chain.proceed(r);
        });
        clientBuilder.cache(cache);
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        clientBuilder.readTimeout(0, TimeUnit.SECONDS);
        return clientBuilder.build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(Application application, Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(server)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
                .create(ApiService.class);
    }
}