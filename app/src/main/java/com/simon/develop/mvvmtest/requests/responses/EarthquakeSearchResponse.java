package com.simon.develop.mvvmtest.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.simon.develop.mvvmtest.models.Earthquake;

import java.util.List;

public class EarthquakeSearchResponse {

    @SerializedName("features")
    @Expose()
    private List<Earthquake> earthquakes;

    public List<Earthquake> getEarthquakes() {
        return earthquakes;
    }

    @Override
    public String toString() {
        return "EarthquakeSearchResponse{" +
                "earthquakes=" + earthquakes +
                '}';
    }
}
