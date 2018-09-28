package com.nerdery.umbrella.data.constants

import android.content.Context
import android.support.annotation.StringRes
import com.google.gson.annotations.SerializedName
import com.nerdery.umbrella.R

/**
 * Temperature unit to be used in requests for [com.nerdery.umbrella.data.api.WeatherApi]
 */
enum class TempUnit private constructor(
  private val value: String,
  @param:StringRes @field:StringRes private var stringRes: Int
) {
  @SerializedName("si")
  CELSIUS("si", R.string.celsius),

  @SerializedName("us")
  FAHRENHEIT("us", R.string.fahrenheit);

  override fun toString(): String {
    return value
  }

  fun toString(context: Context): String {
    return context.getString(stringRes)
  }
}
