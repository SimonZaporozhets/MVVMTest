package com.simon.develop.mvvmtest.persistence;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simon.develop.mvvmtest.models.Earthquake;

import java.lang.reflect.Type;

public class Converters {

    @TypeConverter
    public static String propertiesToString(Earthquake.Properties properties) {
        Gson gson = new Gson();
        String json = gson.toJson(properties);
        return json;
    }

    @TypeConverter
    public static Earthquake.Properties stringToProperties(String value) {
        Type listType = new TypeToken<Earthquake.Properties>(){}.getType();
        return new Gson().fromJson(value, listType);
    }


}
