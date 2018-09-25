package com.nerdery.umbrella.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Temperature unit to be used in requests for {@link com.nerdery.umbrella.data.api.WeatherService}
 */
public enum TempUnit {
    @SerializedName("si")
    CELSIUS("si"),

    @SerializedName("us")
    FAHRENHEIT("us");

    private String value;

    TempUnit(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
