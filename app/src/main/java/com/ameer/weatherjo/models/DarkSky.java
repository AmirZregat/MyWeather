package com.ameer.weatherjo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DarkSky {
    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    @SerializedName("timezone")
    @Expose
    private String timezone;

    @SerializedName("currently")
    @Expose
    private Currently currently;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public Currently getCurrently() {
        return currently;
    }
}
