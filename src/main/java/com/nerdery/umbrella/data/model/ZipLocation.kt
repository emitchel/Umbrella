package com.nerdery.umbrella.data.model

import com.google.gson.annotations.SerializedName

class ZipLocation {
  @SerializedName("z")
  var zipCode: Long = 0
    internal set
  @SerializedName("c")
  var city: String? = null
    internal set
  @SerializedName("s")
  var state: String? = null
    internal set
  @SerializedName("la")
  var latitude: Double = 0.toDouble()
    internal set
  @SerializedName("lo")
  var longitude: Double = 0.toDouble()
    internal set
}