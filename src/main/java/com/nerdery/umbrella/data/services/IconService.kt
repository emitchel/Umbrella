package com.nerdery.umbrella.data.services

/**
 * API for getting custom Nerdery icon URLs for weather conditions
 *
 * This really isn't so much an API as a utility, but we will treat it as an API
 *
 * @author bherbst
 */
class IconService {

  enum class IconType(val value: String) {
    CLEAR("Clear"),
    Overcast("Overcast");

    override fun toString(): String {
      return value
    }
  }

  /**
   * Get the URL to an icon suitable for use as a replacement for the icons given by Weather Underground
   * @param icon The name of the icon provided by Weather Underground (e.g. "clear").
   * @param highlighted True to get the highlighted version, false to get the outline version
   * @return A URL to an icon
   */
  fun getUrlForIcon(
    icon: IconType,
    highlighted: Boolean
  ): String {
    val highlightParam = if (highlighted) "-selected" else ""
    return String.format(
        "https://codechallenge.nerderylabs.com/mobile-nat/%s%s.png", icon, highlightParam
    )
  }
}
