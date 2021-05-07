package com.simon.develop.mvvmtest.viewmodels;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.simon.develop.mvvmtest.models.Earthquake;
import com.simon.develop.mvvmtest.repositories.EarthquakeRepository;
import com.simon.develop.mvvmtest.util.Resource;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = "MainActivityViewModel";
    public static final String QUERY_EXHAUSTED = "No more results.";

    // query extras
    private boolean isQueryExhausted;
    private boolean isPerformingQuery;
    private String format;
    private int limit;
    private double minmag;
    private String orderby;
    private boolean cancelRequest;
    private long requestStartTime;

    private EarthquakeRepository earthquakeRepository;
    private MediatorLiveData<Resource<List<Earthquake>>> earthquakes = new MediatorLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        earthquakeRepository = EarthquakeRepository.getInstance(application);
    }

    public LiveData<Resource<List<Earthquake>>> getEarthquakes() {
        return earthquakes;
    }

    public int getLimit() {
        return limit;
    }

    public void searchEarthquakesApi(String format, int limit, double minmag, String orderby) {
        if (!isPerformingQuery) {
            if(limit == 0) {
                limit = 1;
            }
            this.format = format;
            this.limit = limit;
            this.minmag = minmag;
            this.orderby = orderby;
            isQueryExhausted = false;
            executeSearch();
        }
    }

    public void searchNextItems() {
        if (!isPerformingQuery && !isQueryExhausted) {
            limit += 30;
            executeSearch();
        }
    }

    private void executeSearch() {
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        isPerformingQuery = true;
        final LiveData<Resource<List<Earthquake>>> repositorySource = earthquakeRepository.searchEarthquakesApi(format, limit, minmag, orderby);
        earthquakes.addSource(repositorySource, new Observer<Resource<List<Earthquake>>>() {
            @Override
            public void onChanged(Resource<List<Earthquake>> listResource) {
                if (listResource != null) {
                    earthquakes.setValue(listResource);
                    if (listResource.status == Resource.Status.SUCCESS) {
                        Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds. ");
                        isPerformingQuery = false;
                        if (listResource.data != null) {
                            if (listResource.data.size() == 0) {
                                Log.d(TAG, "onChanged: query is exhausted");
                                earthquakes.setValue(
                                        new Resource<List<Earthquake>>(
                                                Resource.Status.ERROR,
                                                listResource.data,
                                                QUERY_EXHAUSTED
                                        )
                                );
                            }
                        }
                        earthquakes.removeSource(repositorySource);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds. ");
                        isPerformingQuery = false;
                        earthquakes.removeSource(repositorySource);
                    }
                } else {
                    earthquakes.removeSource(repositorySource);
                }
            }
        });
    }
}
