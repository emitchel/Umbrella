package com.nerdery.umbrella.data.services

import android.location.Location

interface ILocationService {

  fun startLocationUpdates(onlyOnce: Boolean)

  fun stopLocationUpdates()

  fun getLastLocation()

  class OnLocationUpdateEvent(val location: Location)
}