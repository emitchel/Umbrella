package com.nerdery.umbrella.data.constants

import android.content.Context
import android.support.annotation.StringRes
import com.nerdery.umbrella.R

enum class Units constructor(@StringRes stringRes: Int) {
  FAHRENHEIT(R.string.fahrenheit),
  CELSIUS(R.string.celsius);

  private var stringRes = -1

  init {
    this.stringRes = stringRes
  }

  fun toString(context: Context): String {
    return context.getString(stringRes)
  }
}
