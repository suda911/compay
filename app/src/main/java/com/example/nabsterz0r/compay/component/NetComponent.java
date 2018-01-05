package com.example.nabsterz0r.compay.component;

import com.example.nabsterz0r.compay.fragment.CityFragment;
import com.example.nabsterz0r.compay.module.AppModule;
import com.example.nabsterz0r.compay.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(CityFragment cityFragment);
}