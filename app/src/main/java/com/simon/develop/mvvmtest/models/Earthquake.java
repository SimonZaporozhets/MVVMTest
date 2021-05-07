package com.simon.develop.mvvmtest.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "earthquakes")
public class Earthquake implements Parcelable {

    @PrimaryKey
    @NonNull
    private String id;

    @Embedded
    public Properties properties;

    protected Earthquake(Parcel in) {
        id = in.readString();
    }

    public static final Creator<Earthquake> CREATOR = new Creator<Earthquake>() {
        @Override
        public Earthquake createFromParcel(Parcel in) {
            return new Earthquake(in);
        }

        @Override
        public Earthquake[] newArray(int size) {
            return new Earthquake[size];
        }
    };

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Earthquake() {
    }

    public Earthquake(Properties properties) {
        this.properties = properties;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        id = dest.readString();
    }

    public static class Properties {
        @ColumnInfo(name = "mag")
        private double mag;
        @ColumnInfo(name = "place")
        private String place;
        @ColumnInfo(name = "time")
        private long time;
        @ColumnInfo(name = "url")
        String url;

        public Properties() {
        }

        public Properties(double mag, String place, long time, String url) {
            this.mag = mag;
            this.place = place;
            this.time = time;
            this.url = url;
        }

        public double getMag() {
            return mag;
        }

        public void setMag(double mag) {
            this.mag = mag;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Properties{" +
                    "mag=" + mag +
                    ", place='" + place + '\'' +
                    ", time=" + time +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "properties=" + properties +
                '}';
    }
}
