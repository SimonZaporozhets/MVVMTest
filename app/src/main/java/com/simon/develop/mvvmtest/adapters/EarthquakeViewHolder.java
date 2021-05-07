package com.simon.develop.mvvmtest.adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.simon.develop.mvvmtest.R;
import com.simon.develop.mvvmtest.models.Earthquake;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EarthquakeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView magTextView;
    public TextView placeKmTextView;
    public TextView placePointTextView;
    public TextView timeTextView;
    OnEarthquakeListener onEarthquakeListener;
    RequestManager requestManager;

    public EarthquakeViewHolder(View itemView,
                                OnEarthquakeListener onEarthquakeListener,
                                RequestManager requestManager) {
        super(itemView);
        this.requestManager = requestManager;
        magTextView = (TextView) itemView.findViewById(R.id.mag);
        placeKmTextView = (TextView) itemView.findViewById(R.id.place_km);
        placePointTextView = (TextView) itemView.findViewById(R.id.place_point);
        timeTextView = (TextView) itemView.findViewById(R.id.time);

        this.onEarthquakeListener = onEarthquakeListener;

        itemView.setOnClickListener(this);
    }

    public void onBind(Earthquake earthquake, Context context) {
        // set magnitude value

        DecimalFormat formatter = new DecimalFormat("0.0");

        String magnitude = formatter.format(earthquake.properties.getMag());

        magTextView.setText(magnitude);

        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earthquake.properties.getMag(), context);
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // set km value

        String place = earthquake.properties.getPlace();
        String[] splitPlace = null;

        if (place.contains("of")) {
            splitPlace = place.split(" of ");
            placeKmTextView.setText(splitPlace[0] + " of ");
        } else {
            placeKmTextView.setText("Near the");
        }

        // set city value
        if (splitPlace != null) {
            placePointTextView.setText(splitPlace[1]);
        } else {
            placePointTextView.setText(place);
        }

        // set date and time value

        Date date = new Date(earthquake.properties.getTime());

        SimpleDateFormat format = new SimpleDateFormat("MMM dd,YYYY\nK:mm a", new Locale("en", "EN"));

        String time = format.format(date);

        timeTextView.setText(time);
    }

    private int getMagnitudeColor(double mag, Context context) {

        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(mag);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(context , magnitudeColorResourceId);
    }

    @Override
    public void onClick(View v) {
        onEarthquakeListener.onEarthquakeClick(getAdapterPosition());
    }
}