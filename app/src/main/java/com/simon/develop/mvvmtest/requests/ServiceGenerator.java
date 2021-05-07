package com.simon.develop.mvvmtest.requests;

import com.simon.develop.mvvmtest.util.Constants;
import com.simon.develop.mvvmtest.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.simon.develop.mvvmtest.util.Constants.CONNECTION_TIMEOUT;
import static com.simon.develop.mvvmtest.util.Constants.READ_TIMEOUT;
import static com.simon.develop.mvvmtest.util.Constants.WRITE_TIMEOUT;

public class ServiceGenerator {

    private static OkHttpClient client = new OkHttpClient.Builder()
            //establish connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            // time between byte read from the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            // time between each byte sent  to server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            .retryOnConnectionFailure(false)

            .build();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static EarthquakeApi earthquakeApi = retrofit.create(EarthquakeApi.class);

    public static EarthquakeApi getEarthquakeApi() {
        return earthquakeApi;
    }
}
