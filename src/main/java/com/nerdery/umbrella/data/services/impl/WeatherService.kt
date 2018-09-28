package com.nerdery.umbrella.data.services.impl

import com.nerdery.umbrella.R
import com.nerdery.umbrella.data.api.WeatherApi
import com.nerdery.umbrella.data.constants.TempUnit
import com.nerdery.umbrella.data.constants.TempUnit.FAHRENHEIT
import com.nerdery.umbrella.data.services.IWeatherService
import com.nerdery.umbrella.data.services.IWeatherService.GetWeatherForLatLongTempUnitEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class WeatherService(
  private val weatherApi: WeatherApi,
  private val eventBus: EventBus
) : IWeatherService {

  override fun getColorForTemperatureAndUnit(
    temp: Double,
    tempUnit: TempUnit
  ): Int {
    return if (tempUnit == FAHRENHEIT && temp >= 60) {
      R.color.weather_warm
    } else {
      R.color.weather_cool
    }
  }

  override fun getWeatherForLatLongTempUnit(
    latitude: Double,
    longitude: Double,
    tempUnit: TempUnit
  ) {
    weatherApi.getWeather(latitude, longitude, tempUnit)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe { response, throwable ->
          eventBus.post(
              GetWeatherForLatLongTempUnitEvent(latitude, longitude, tempUnit, throwable, response)
          )
        }
  }
}
