package com.nerdery.umbrella.data.model

import com.google.gson.annotations.SerializedName

import com.nerdery.umbrella.data.constants.TempUnit
import java.util.Date

/**
 * Specific weather condition for time and location
 */
class ForecastCondition {
  /**
   * Text summary of weather condition
   * @return Summary
   */
  var summary: String? = null
    internal set
  /**
   * Icon name of weather condition
   * @return Icon Name
   */
  var icon: String? = null
    internal set
  /**
   * Temperature in degrees of [TempUnit] sent during request
   * @return Temperature
   */
  @SerializedName("temperature")
  var temp: Double = 0.toDouble()
    internal set
  /**
   * Time/Date of Forecast Condition
   * @return Date
   */
  lateinit var time: Date
    internal set

  var highestTemp = false
  var lowestTemp = false
}
