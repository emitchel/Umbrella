package com.nerdery.umbrella.data.services

interface IIconService {

  enum class IconType(val value: String) {
    CLEAR("Clear"),
    Overcast("Overcast");

    override fun toString(): String {
      return value
    }

    fun getTypeByStrin(value: String): IconType? {
      enumValues<IconType>().forEach {
        if (it.value == value) return it
      }
      return null
    }
  }

  fun getUrlForIcon(
    icon: IconType,
    highlighted: Boolean
  ): String

}