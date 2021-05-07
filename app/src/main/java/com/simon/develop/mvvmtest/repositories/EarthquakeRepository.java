package com.simon.develop.mvvmtest.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.simon.develop.mvvmtest.AppExecutors;
import com.simon.develop.mvvmtest.models.Earthquake;
import com.simon.develop.mvvmtest.persistence.EarthquakeDao;
import com.simon.develop.mvvmtest.persistence.EarthquakeDatabase;
import com.simon.develop.mvvmtest.requests.ServiceGenerator;
import com.simon.develop.mvvmtest.requests.responses.ApiResponse;
import com.simon.develop.mvvmtest.requests.responses.EarthquakeSearchResponse;
import com.simon.develop.mvvmtest.util.NetworkBoundResource;
import com.simon.develop.mvvmtest.util.Resource;

import java.util.List;

public class EarthquakeRepository {

    private static final String TAG = "EarthquakeRepository";

    private static EarthquakeRepository instance;
    private static EarthquakeDao earthquakeDao;

    public static EarthquakeRepository getInstance(Context context) {
        if(instance == null) {
            instance = new EarthquakeRepository(context);
        }
        return instance;
    }

    private EarthquakeRepository(Context context) {
        earthquakeDao = EarthquakeDatabase.getInstance(context).getEarthquakeDao();
    }

    public LiveData<Resource<List<Earthquake>>> searchEarthquakesApi(final String format, final int limit, final double minmag, final String orderby) {
        return new NetworkBoundResource<List<Earthquake>, EarthquakeSearchResponse>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull EarthquakeSearchResponse item) {

                if (item.getEarthquakes() != null) {


                    Earthquake[] earthquakes = new Earthquake[item.getEarthquakes().size()];

                    earthquakeDao.insertEarthquakes((Earthquake[]) item.getEarthquakes().toArray(earthquakes));
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Earthquake> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Earthquake>> loadFromDb() {
                return earthquakeDao.searchEarthquakes(limit, minmag);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<EarthquakeSearchResponse>> createCall() {
                return ServiceGenerator.getEarthquakeApi()
                        .queryEarthquake(
                                format,
                                String.valueOf(limit),
                                String.valueOf(minmag),
                                orderby
                        );
            }
        }.getAsLiveData();
    }
}
