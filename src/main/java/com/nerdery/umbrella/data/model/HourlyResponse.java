package com.nerdery.umbrella.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HourlyResponse {
    @SerializedName("data")
    List<ForecastCondition> hours;

    /**
     * Ordered List of Hourly {@link ForecastCondition}
     * @return List of {@link ForecastCondition}
     */
    public List<ForecastCondition> getHours() {
        return hours;
    }
}
