package com.nerdery.umbrella.data.services

import android.content.Context
import com.nerdery.umbrella.data.model.ZipLocation

interface IZipCodeService {
  interface ZipLocationListener {
    fun onLocationFound(location: ZipLocation)
    fun onLocationNotFound()
  }

  fun getLatLongByZip(
    context: Context,
    zipCode: String,
    listener: ZipLocationListener
  )
}