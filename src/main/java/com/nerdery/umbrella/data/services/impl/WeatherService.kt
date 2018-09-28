package com.nerdery.umbrella.data.services.impl

import com.nerdery.umbrella.R
import com.nerdery.umbrella.data.api.WeatherApi
import com.nerdery.umbrella.data.constants.TempUnit
import com.nerdery.umbrella.data.constants.TempUnit.FAHRENHEIT
import com.nerdery.umbrella.data.model.DayForecastCondition
import com.nerdery.umbrella.data.services.IWeatherService
import com.nerdery.umbrella.data.services.IWeatherService.GetWeatherForLatLongTempUnitEvent
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.util.Calendar
import java.util.Date

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
        .observeOn(Schedulers.computation())
        .subscribeOn(Schedulers.io())
        .subscribe { response, throwable ->

          //we want to get a grouping of forecasts by day
          val groupedByDay =
            response?.hourly?.hours?.groupBy { dateToCalendar(it.time).get(Calendar.DAY_OF_YEAR) }

          val dayForecastCondition = groupedByDay?.map {
            DayForecastCondition(it.value[0].time, it.value)
          }

          dayForecastCondition?.let { dayForecastCondition ->

            //now set the highest and lowest
            dayForecastCondition.forEach { dayGrouping ->
              dayGrouping.forecastConditions.maxBy { it.temp }
                  ?.highestTemp = true
              dayGrouping.forecastConditions.minBy { it.temp }
                  ?.lowestTemp = true
            }
            response.dayForecastConditions = dayForecastCondition
          }

          eventBus.post(
              GetWeatherForLatLongTempUnitEvent(latitude, longitude, tempUnit, throwable, response)
          )
        }
        .dispose()
  }
}

//TODO move to util and unit test
private fun dateToCalendar(date: Date): Calendar {

  val calendar = Calendar.getInstance()
  calendar.time = date
  return calendar

}
