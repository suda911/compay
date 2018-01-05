package com.example.nabsterz0r.compay;

import android.content.Context;

import com.example.nabsterz0r.compay.model.City;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;


public class Util {

    private static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.cites);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, context.getString(R.string.utf));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<City> getCity(Context c) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<City>>() {}.getType();
        return gson.fromJson(loadJSONFromAsset(c), listType);
    }

}
