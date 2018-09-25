package com.nerdery.umbrella.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response from DarkSky weather requests in {@link com.nerdery.umbrella.data.api.WeatherService}
 */
public class WeatherResponse {

    @SerializedName("currently")
    ForecastCondition currentForecast;

    HourlyResponse hourly;

    /**
     * Current Weather Condition
     * @return ForecastCondition
     */
    public ForecastCondition getCurrentForecast() {
        return currentForecast;
    }

    /**
     * Hourly Response model that contains list of ForecastConditions
     * @return HourlyResponse
     */
    public HourlyResponse getHourly() {
        return hourly;
    }
}
