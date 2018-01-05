package com.example.nabsterz0r.compay.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Main {

        @SerializedName("temp")
        @Expose
        private Double temp;
        @SerializedName("pressure")
        @Expose
        private Double pressure;
        @SerializedName("humidity")
        @Expose
        private Double humidity;
        @SerializedName("temp_min")
        @Expose
        private Double tempMin;
        @SerializedName("temp_max")
        @Expose
        private Double tempMax;

        public Double getTemp() {
            return temp;
        }

        public void setTemp(Double temp) {
            this.temp = temp;
        }

        public String getTempString(){
            return String.valueOf(temp);
        }

        public String getPicturePath(){
            if(getTemp()>=0){
                return "http://rifey.ru/sites/default/files/45634647.jpg";
            }else{
                return "https://nerdist.com/wp-content/uploads/2015/08/Cold-Weather-Movies-082215.jpg";
            }
        }


    }

}
