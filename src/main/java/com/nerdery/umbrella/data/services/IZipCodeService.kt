package com.nerdery.umbrella.data.services

import android.content.Context
import android.location.Location
import com.nerdery.umbrella.data.model.ZipLocation

interface IZipCodeService {
  interface ZipLocationListener {
    fun onLocationFound(location: ZipLocation)
    fun onLocationNotFound()
  }

  fun findAndSetClosestZipToLocation(location: Location?)

  fun initDatabase(context: Context)

  fun getLatLongByZip(
    zipCode: String,
    listener: ZipLocationListener
  )
}