package com.example.nabsterz0r.compay.model.orm;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Weather {
    @Id(assignable = true)
    long id;

    Double weather;
    String name;

    public Weather(long id, Double weather, String name) {
        this.id = id;
        this.weather = weather;
        this.name = name;
    }

    public Double getWeather() {
        return weather;
    }

    public String getName() {
        return name;
    }

}
