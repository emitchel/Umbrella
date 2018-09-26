package com.nerdery.umbrella.data.services.impl

import android.content.Context
import com.nerdery.umbrella.data.services.ILocationService
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationParams
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import org.greenrobot.eventbus.EventBus

class LocationService(
  applicationContext: Context,
  private val eventBus: EventBus
) : ILocationService {
  private val smartLocation: SmartLocation = SmartLocation.Builder(applicationContext)
      .logging(true)
      .build()
  private val provider: LocationGooglePlayServicesProvider = LocationGooglePlayServicesProvider()

  private var onLocationUpdatedListener: OnLocationUpdatedListener? = null

  init {
    provider.setCheckLocationSettings(true)
    initListeners()
  }

  //region Location Service Overrides

  override fun startLocationUpdates(onlyOnce: Boolean) {
    if (onlyOnce) {
      smartLocation.location()
          .oneFix()
          .start(onLocationUpdatedListener)
    } else {
      smartLocation.location()
          .config(LocationParams.NAVIGATION)
          .start(onLocationUpdatedListener)
    }
  }

  override fun stopLocationUpdates() {
    smartLocation.location()
        .stop()
  }

  override fun getLastLocation() {
    eventBus.post(ILocationService.OnLocationUpdateEvent(smartLocation.location().lastLocation!!))
  }

  //endregion

  private fun initListeners() {
    onLocationUpdatedListener = OnLocationUpdatedListener { location ->
      eventBus.post(ILocationService.OnLocationUpdateEvent(location))
    }
  }
}