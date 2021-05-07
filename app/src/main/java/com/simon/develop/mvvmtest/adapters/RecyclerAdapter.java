package com.simon.develop.mvvmtest.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.simon.develop.mvvmtest.R;
import com.simon.develop.mvvmtest.models.Earthquake;
import com.simon.develop.mvvmtest.adapters.EarthquakeViewHolder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EARTHQUAKE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int EXHAUSTED_TYPE = 3;

    private List<Earthquake> mEarthquakes;
    private OnEarthquakeListener onEarthquakeListener;
    Context mContext;
    private RequestManager requestManager;

    public RecyclerAdapter(Context context, OnEarthquakeListener onEarthquakeListener, RequestManager requestManager) {
        this.onEarthquakeListener = onEarthquakeListener;
        this.mContext = context;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;
        switch (i) {
            case EARTHQUAKE_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
                return new EarthquakeViewHolder(view, onEarthquakeListener, requestManager);
            }
            case LOADING_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_view, viewGroup, false);
                return new LoadingViewHolder(view);
            }
            case EXHAUSTED_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search_exhausted, viewGroup, false);
                return new SearchExhaustedViewHolder(view);
            }
            default: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
                return new EarthquakeViewHolder(view, onEarthquakeListener, requestManager);
            }
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        if (itemViewType == EARTHQUAKE_TYPE) {
            ((EarthquakeViewHolder) viewHolder).onBind(mEarthquakes.get(i), mContext);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mEarthquakes.get(position).properties.getPlace().equals("LOADING...")) {
            return LOADING_TYPE;
        } else if(mEarthquakes.get(position).properties.getPlace().equals("EXHAUSTED...")) {
            return EXHAUSTED_TYPE;
        } else {
            return EARTHQUAKE_TYPE;
        }
    }

    // display loading during search request
    public void displayOnlyLoading() {
        clearEarthquakeList();
        Earthquake earthquake = new Earthquake();
        Earthquake.Properties properties = new Earthquake.Properties();
        earthquake.setProperties(properties);
        earthquake.properties.setPlace("LOADING...");
        mEarthquakes.add(earthquake);
        notifyDataSetChanged();
    }

    private void clearEarthquakeList() {
        if (mEarthquakes == null) {
            mEarthquakes = new ArrayList<>();
        } else {
            mEarthquakes.clear();
        }
        notifyDataSetChanged();
    }

    public void setQueryExhausted() {
        hideLoading();
        Earthquake exhaustedEarthquake = new Earthquake();
        Earthquake.Properties properties = new Earthquake.Properties();
        exhaustedEarthquake.setProperties(properties);
        exhaustedEarthquake.properties.setPlace("EXHAUSTED...");
        mEarthquakes.add(exhaustedEarthquake);
        notifyDataSetChanged();
    }

    public void hideLoading() {
        if(isLoading()) {
            if (mEarthquakes.get(0).properties.getPlace().equals("LOADING...")) {
                mEarthquakes.remove(0);
            }
            else if (mEarthquakes.get(mEarthquakes.size() - 1).properties.getPlace().equals("LOADING")) {
                mEarthquakes.remove(mEarthquakes.size() - 1);
            }
            notifyDataSetChanged();
        }
    }

    //pagination loading
    public void displayLoading() {
        if (mEarthquakes == null) {
            mEarthquakes = new ArrayList<>();
        }

        if (!isLoading()) {
            Earthquake earthquake = new Earthquake();
            Earthquake.Properties properties = new Earthquake.Properties();
            earthquake.setProperties(properties);
            earthquake.properties.setPlace("LOADING...");
            mEarthquakes.add(earthquake);
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (mEarthquakes != null) {
            if (mEarthquakes.size() > 0) {
                if (mEarthquakes.get(mEarthquakes.size() - 1).properties.getPlace().equals("LOADING...")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (mEarthquakes != null) {
            return mEarthquakes.size();
        }
        return 0;
    }

    public void setEarthquakes(List<Earthquake> earthquakes) {
        mEarthquakes = earthquakes;
        notifyDataSetChanged();
    }

    public Earthquake getSelectedEarthquake(int position) {
        if (mEarthquakes != null) {
            if(mEarthquakes.size() > 0) {
                return mEarthquakes.get(position);
            }
        }
        return null;
    }

}
