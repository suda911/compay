package com.example.nabsterz0r.compay;

import com.example.nabsterz0r.compay.model.WeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiService {

    @GET("2.5/weather/?units=metric")
    Observable<WeatherResponse> info(
            @Query("id") int id
    );

}
