package com.nerdery.umbrella.data.services

import android.support.annotation.ColorRes
import com.nerdery.umbrella.data.constants.TempUnit
import com.nerdery.umbrella.data.model.WeatherResponse

interface IWeatherService {
  fun getWeatherForLatLongTempUnit(
    latitude: Double,
    longitude: Double,
    tempUnit: TempUnit
  )

  @ColorRes
  fun getColorForTemperatureAndUnit(temp:Double, tempUnit: TempUnit) : Int

  class GetWeatherForLatLongTempUnitEvent(
    val latitude: Double = 0.toDouble(),
    val longitude: Double = 0.toDouble(),
    val tempUnitUsed: TempUnit? = null,
    val throwable: Throwable? = null,
    val response: WeatherResponse? = null
  )

}
