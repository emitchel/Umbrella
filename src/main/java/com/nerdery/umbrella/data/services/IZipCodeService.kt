package com.nerdery.umbrella.data.services

import android.content.Context
import android.location.Location
import com.nerdery.umbrella.data.model.ZipLocation

interface IZipCodeService {

  fun findZipLocationsClosestToLocation(location: Location?)

  fun initDatabase(context: Context)

  fun getZipLocationByZip(
    zipCode: Long
  )

  class GetZipLocationByZipEvent(
    val location: ZipLocation?
  )

  class FoundZipLocationsClosestToLocationEvent(
    val location: Location?,
    val zipLocations: List<ZipLocation>?
  )
}