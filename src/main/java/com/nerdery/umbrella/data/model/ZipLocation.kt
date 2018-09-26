package com.nerdery.umbrella.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class ZipLocation {
  constructor( //for Room's satisfaction
    zipCode: Long,
    city: String?,
    state: String?,
    latitude: Double,
    longitude: Double
  ) {
    this.zipCode = zipCode
    this.city = city
    this.state = state
    this.latitude = latitude
    this.longitude = longitude
  }

  @PrimaryKey
  @SerializedName("z")
  var zipCode: Long = 0
  @SerializedName("c")
  var city: String? = null
  @SerializedName("s")
  var state: String? = null
  @SerializedName("la")
  var latitude: Double = 0.toDouble()
  @SerializedName("lo")
  var longitude: Double = 0.toDouble()
}