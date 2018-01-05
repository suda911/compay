package com.example.nabsterz0r.compay;

import android.app.Application;

import com.example.nabsterz0r.compay.component.DaggerNetComponent;
import com.example.nabsterz0r.compay.component.NetComponent;
import com.example.nabsterz0r.compay.model.orm.MyObjectBox;
import com.example.nabsterz0r.compay.module.AppModule;
import com.example.nabsterz0r.compay.module.NetModule;

import io.objectbox.BoxStore;


public class MyApp extends Application {
    private static NetComponent mNetComponent;

    public BoxStore boxStore;


    @Override
    public void onCreate() {
    super.onCreate();
    boxStore = MyObjectBox.builder().androidContext(this).build();
    mNetComponent = DaggerNetComponent.builder()
            .appModule(new AppModule(this))
            .netModule(new NetModule(getString(R.string.server), getString(R.string.api_key)))
            .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }


    public BoxStore getBoxStore() {
        return boxStore;
    }


}