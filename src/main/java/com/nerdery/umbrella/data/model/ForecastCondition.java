package com.nerdery.umbrella.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Specific weather condition for time and location
 */
public class ForecastCondition {
    String summary;
    String icon;
    @SerializedName("temperature")
    double temp;
    Date time;

    /**
     * Text summary of weather condition
     * @return Summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Icon name of weather condition
     * @return Icon Name
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Temperature in degrees of {@link TempUnit} sent during request
     * @return Temperature
     */
    public double getTemp() {
        return temp;
    }

    /**
     * Time/Date of Forecast Condition
     * @return Date
     */
    public Date getTime() {
        return time;
    }
}
