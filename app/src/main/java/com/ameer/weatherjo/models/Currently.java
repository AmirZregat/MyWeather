package com.ameer.weatherjo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Currently {
    @SerializedName("summary")
    @Expose
    private String summary;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("apparentTemperature")
    @Expose
    private double apparentTemperature;

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    @SerializedName("precipProbability")
    @Expose
    private double precipProbability;

    @SerializedName("precipType")
    @Expose
    private String precipType;

    @SerializedName("temperature")
    @Expose
    private double temperature;

    @SerializedName("humidity")
    @Expose
    private double humidity;

    @SerializedName("windSpeed")
    @Expose
    private double windSpeed;


    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public double getPrecipProbability() {
        return precipProbability;
    }

    public String getPrecipType() {
        return precipType;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}
