package com.nerdery.umbrella.data.model

import java.util.Date

class DayForecastCondition(
  val day: Date,
  val forecastConditions: List<ForecastCondition>
)