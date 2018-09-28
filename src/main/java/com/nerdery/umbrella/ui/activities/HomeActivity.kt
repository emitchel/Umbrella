package com.nerdery.umbrella.ui.activities

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import com.f2prateek.rx.preferences.RxSharedPreferences
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nerdery.umbrella.R
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.constants.SettingKeys
import com.nerdery.umbrella.data.constants.ZipCodes
import com.nerdery.umbrella.data.model.ZipLocation
import com.nerdery.umbrella.data.services.ILocationService
import com.nerdery.umbrella.data.services.IZipCodeService
import com.nerdery.umbrella.data.services.IZipCodeService.FoundZipLocationsClosestToLocationEvent
import com.nerdery.umbrella.data.services.IZipCodeService.GetZipLocationByZipEvent
import com.nerdery.umbrella.ui.activities.base.BaseActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import timber.log.Timber
import javax.inject.Inject

class HomeActivity : BaseActivity() {

  @Inject lateinit var rxSharedPreferences: RxSharedPreferences
  @Inject lateinit var sharedPreferences: SharedPreferences
  @Inject lateinit var locationService: ILocationService
  @Inject lateinit var eventBus: EventBus
  @Inject lateinit var zipCodeService: IZipCodeService

  private var zipCode: Observable<Long>? = null
  private var currentZipLocation: ZipLocation? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    UmbrellaApp.INSTANCE?.component?.inject(this)
    eventBus.register(this)
    setContentView(R.layout.activity_home)
    setupData()
  }

  private fun setupData() {
    if (!sharedPreferences.getBoolean(SettingKeys.USER_ONBOARDED, false)) {
      sharedPreferences.edit()
          .putBoolean(SettingKeys.USER_ONBOARDED, true)
          .apply()
      //need to attempt and get user's location first
      Dexter.withActivity(this)
          .withPermissions(
              Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
          )
          .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
              if (report?.areAllPermissionsGranted() == true) {
                //lets get location and try to find the closest zip code, see [onEvent(event: ILocationService.OnLocationUpdateEvent)]
                showProgressDialog()
                locationService.startLocationUpdates(true)
              } else {
                //defaulting to zip
                sharedPreferences.edit()
                    .putLong(SettingKeys.ZIP, ZipCodes.DEFAULT_ZIPCODE)
                    .apply()
                //recursively call so we can get observable setup for future changes to zip code
                setupData()
              }
            }

            override fun onPermissionRationaleShouldBeShown(
              permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
              token: PermissionToken?
            ) {
              token?.continuePermissionRequest()
            }

          })
          .check()
    } else {
      zipCode = rxSharedPreferences.getLong(SettingKeys.ZIP, ZipCodes.DEFAULT_ZIPCODE)
          .asObservable()
      zipCode?.let { observable ->
        observable
            .subscribe {
              showProgressDialog()
              //make a db request to get ziplocation see [ fun onEvent(event: GetZipLocationByZipEvent) ]
              zipCodeService.getZipLocationByZip(it)
            }
      }
    }
  }

  private fun setZipLocation(zipLocation: ZipLocation?) {
    zipLocation?.let {
      currentZipLocation = it
      setupUI()
    } ?: run {
      //TODO: not found scenario, take them back to settings page? toast an Error?
    }
  }

  private fun setupUI() {

  }

  @Subscribe
  fun onEvent(event: ILocationService.OnLocationUpdateEvent) {
    Timber.i("Got location, ${event.location}")
    //now that we have location, try to get the closest zipcode(s)
    //see [onEvent(event: FoundZipLocationsClosestToLocationEvent)]
    zipCodeService.findZipLocationsClosestToLocation(event.location)
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onEvent(event: FoundZipLocationsClosestToLocationEvent) {
    hideProgressDialog()
    //tODo show a list of nearby zip codes for the to select from

    //savings the default zipcode
    sharedPreferences.edit()
        .putLong(
            SettingKeys.ZIP, if (event.zipLocations != null && event.zipLocations.isNotEmpty())
          event.zipLocations[0].zipCode
        else
          ZipCodes.DEFAULT_ZIPCODE
        )
        .apply()

    setupData()
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onEvent(event: GetZipLocationByZipEvent) {
    hideProgressDialog()
    setZipLocation(event.location)
  }
}
