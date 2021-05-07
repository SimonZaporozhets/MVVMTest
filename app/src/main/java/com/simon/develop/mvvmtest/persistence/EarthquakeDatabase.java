package com.simon.develop.mvvmtest.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.simon.develop.mvvmtest.models.Earthquake;

@Database(entities =  {Earthquake.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class EarthquakeDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "earthquakes_db";

    private static EarthquakeDatabase instance;

    public static EarthquakeDatabase getInstance(final Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    EarthquakeDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public  abstract EarthquakeDao getEarthquakeDao();
}
