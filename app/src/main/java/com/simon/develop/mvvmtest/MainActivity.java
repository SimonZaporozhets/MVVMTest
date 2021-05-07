package com.simon.develop.mvvmtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.simon.develop.mvvmtest.adapters.OnEarthquakeListener;
import com.simon.develop.mvvmtest.adapters.RecyclerAdapter;
import com.simon.develop.mvvmtest.models.Earthquake;
import com.simon.develop.mvvmtest.util.Resource;
import com.simon.develop.mvvmtest.util.VerticalSpacingItemDecorator;
import com.simon.develop.mvvmtest.viewmodels.MainActivityViewModel;

import java.util.List;

import static com.simon.develop.mvvmtest.viewmodels.MainActivityViewModel.QUERY_EXHAUSTED;

public class MainActivity extends AppCompatActivity implements OnEarthquakeListener {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private MainActivityViewModel mMainActivityViewModel;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mSearchView = findViewById(R.id.search_view);

        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        initRecyclerView();
        initSearchView();
        subscribeObservers();

    }

    private void subscribeObservers() {
        
        mMainActivityViewModel.getEarthquakes().observe(this, new Observer<Resource<List<Earthquake>>>() {
            @Override
            public void onChanged(Resource<List<Earthquake>> listResource) {
                if (listResource != null) {
                    Log.d(TAG, "status: " + listResource.status);
                    if (listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING: {
                                if (mMainActivityViewModel.getLimit() > 1) {
                                    mAdapter.displayLoading();
                                } else {
                                    mAdapter.displayOnlyLoading();
                                }
                                break;
                            }
                            case ERROR: {
                                Log.d(TAG, "onChanged: connot refresh the cache.");
                                Log.d(TAG, "onChanged: ERROR message: " + listResource.message);
                                Log.d(TAG, "onChanged: status: ERROR, #earthquakes: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setEarthquakes(listResource.data);
                                Toast.makeText(MainActivity.this, listResource.message, Toast.LENGTH_SHORT).show();

                                if(listResource.message.equals(QUERY_EXHAUSTED)) {
                                    mAdapter.setQueryExhausted();
                                }

                                break;
                            }
                            case SUCCESS: {
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #Earthquakes: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setEarthquakes(listResource.data);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    private RequestManager initGlide() {
        return Glide.with(this);
    }

    private void searchEarthquakesApi(String format, int limit, double minmag, String orderby) {
        mRecyclerView.smoothScrollToPosition(0);
        mMainActivityViewModel.searchEarthquakesApi(format, limit, minmag, orderby);
        mSearchView.clearFocus();
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerAdapter(this, this, initGlide());
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!mRecyclerView.canScrollVertically(1)) {
                    mMainActivityViewModel.searchNextItems();
                }
            }
        });
        searchEarthquakesApi("geojson", 30, 4, "time");
    }

    private void initSearchView() {
       mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(isDouble(query)) {
                    searchEarthquakesApi("geojson", 30, Double.parseDouble(query), "time");
                    mAdapter.displayOnlyLoading();
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect value.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onEarthquakeClick(int position) {
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        intent.putExtra("url", mAdapter.getSelectedEarthquake(position).properties.getUrl());
        startActivity(intent);
    }
}