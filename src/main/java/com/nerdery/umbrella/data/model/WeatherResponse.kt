package com.nerdery.umbrella.data.model

import android.support.annotation.Nullable
import com.google.gson.annotations.SerializedName

/**
 * Response from DarkSky weather requests in [com.nerdery.umbrella.data.api.WeatherApi]
 */
class WeatherResponse {

  /**
   * Current Weather Condition
   * @return ForecastCondition
   */
  @SerializedName("currently")
  var currentForecast: ForecastCondition? = null
    internal set

  /**
   * Hourly Response model that contains list of ForecastConditions
   * @return HourlyResponse
   */
  var hourly: HourlyResponse? = null
    internal set

  @Nullable
  var dayForecastConditions: List<DayForecastCondition>? = null
}
