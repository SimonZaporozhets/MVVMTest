package com.simon.develop.mvvmtest.requests;

import androidx.lifecycle.LiveData;

import com.simon.develop.mvvmtest.requests.responses.ApiResponse;
import com.simon.develop.mvvmtest.requests.responses.EarthquakeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EarthquakeApi {

    // QUERY
    @GET("/fdsnws/event/1/query")
    LiveData<ApiResponse<EarthquakeSearchResponse>> queryEarthquake(
            @Query("format") String format,
            @Query("limit") String limit,
            @Query("minmag") String minmag,
            @Query("orderby") String orderby
    );


}
