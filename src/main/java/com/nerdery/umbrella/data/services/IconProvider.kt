package com.nerdery.umbrella.data.services

/**
 * API for getting custom Nerdery icon URLs for weather conditions
 */
class IconProvider {

  enum class IconType {
    NORMAL,
    HIGHLIGHTED
  }

  /**
   * Get the URL to an icon suitable for use as a replacement for the icons given by Weather Underground
   * @param icon The name of the icon provided by Weather Underground (e.g. "clear").
   * @param type the type of icon to retrieve.
   * @return A URL to an icon
   */
  @JvmOverloads fun getUrlForIcon(
    icon: String,
    type: IconType = IconType.NORMAL
  ): String {
    val highlightParam = if (type == IconType.HIGHLIGHTED) "-selected" else ""
    return String.format(ICON_URL, icon, highlightParam)
  }

  companion object {

    private val ICON_URL = "https://codechallenge.nerderylabs.com/mobile-nat/%s%s.png"
  }
}
/**
 * Get the URL to an icon suitable for use as a replacement for the icons given by Weather Underground
 * @param icon The name of the icon provided by Weather Underground (e.g. "clear").
 * @return A URL to an icon, always returns the non-highlighted version
 */
