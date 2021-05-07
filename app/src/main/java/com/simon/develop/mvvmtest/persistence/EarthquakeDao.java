package com.simon.develop.mvvmtest.persistence;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.simon.develop.mvvmtest.models.Earthquake;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EarthquakeDao {

    @Insert(onConflict = IGNORE)
    long[] insertEarthquakes(Earthquake... earthquakes);

    @Query("UPDATE earthquakes SET mag = :mag, place = :place, time = :time, url = :url " +
    "WHERE id = :id")
    void updateEarthquake(String id, double mag, String place, long time, String url);

    @Query("SELECT * FROM earthquakes WHERE mag > :minmag ORDER BY time DESC LIMIT :limit")
    LiveData<List<Earthquake>> searchEarthquakes(int limit, double minmag);



    }

