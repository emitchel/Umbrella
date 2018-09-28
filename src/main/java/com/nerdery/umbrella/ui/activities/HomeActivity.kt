package com.nerdery.umbrella.ui.activities

import android.Manifest.permission
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import com.f2prateek.rx.preferences.RxSharedPreferences
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nerdery.umbrella.R
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.api.WeatherApi
import com.nerdery.umbrella.data.constants.SettingKeys
import com.nerdery.umbrella.data.constants.TempUnit
import com.nerdery.umbrella.data.constants.TempUnit.FAHRENHEIT
import com.nerdery.umbrella.data.constants.ZipCodes
import com.nerdery.umbrella.data.model.WeatherResponse
import com.nerdery.umbrella.data.model.ZipLocation
import com.nerdery.umbrella.data.services.ILocationService
import com.nerdery.umbrella.data.services.IZipCodeService
import com.nerdery.umbrella.data.services.IZipCodeService.FoundZipLocationsClosestToLocationEvent
import com.nerdery.umbrella.data.services.IZipCodeService.GetZipLocationByZipEvent
import com.nerdery.umbrella.ui.activities.base.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.cl_parent
import kotlinx.android.synthetic.main.activity_home.loading_indicator
import kotlinx.android.synthetic.main.activity_home.tv_location
import kotlinx.android.synthetic.main.activity_home.tv_temp_subtitle
import kotlinx.android.synthetic.main.activity_home.tv_temperature
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.HttpException
import rx.Observable
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class HomeActivity : BaseActivity() {

  @Inject lateinit var rxSharedPreferences: RxSharedPreferences
  @Inject lateinit var sharedPreferences: SharedPreferences
  @Inject lateinit var locationService: ILocationService
  @Inject lateinit var eventBus: EventBus
  @Inject lateinit var zipCodeService: IZipCodeService
  @Inject lateinit var weatherApi: WeatherApi

  private var disposable = CompositeDisposable()
  private var zipCodeObservable: Observable<Long>? = null
  private var tempUnitObservable: Observable<TempUnit>? = null
  private var currentTempUnit: TempUnit = FAHRENHEIT
  private var currentZipLocation: ZipLocation? = null
  private var currentWeatherResponse: WeatherResponse? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    UmbrellaApp.INSTANCE?.component?.inject(this)
    eventBus.register(this)
    setContentView(R.layout.activity_home)
    setupData()
  }

  override fun onStart() {
    super.onStart()
    if (currentZipLocation == null) {
      setupData()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
  }

  private fun setupData() {
    //hiding attributes until they're loaded
    //TODO animate all the transitions...
    tv_location.visibility = View.INVISIBLE
    tv_temp_subtitle.visibility = View.INVISIBLE
    tv_temperature.visibility = View.INVISIBLE
    setupCurrentTempUnit()
    setupCurrentZipLocation()
  }

  private fun setupCurrentTempUnit() {
    tempUnitObservable = rxSharedPreferences.getEnum(
        SettingKeys.TEMP_UNIT, FAHRENHEIT, TempUnit::class.java
    )
        .asObservable()
    tempUnitObservable?.subscribe { tempUnit ->
      currentTempUnit = tempUnit
      currentZipLocation?.let {
        getWeatherForZipLocation(it)
      }
    }
  }

  private fun setupCurrentZipLocation() {
    if (!sharedPreferences.getBoolean(SettingKeys.USER_ONBOARDED, false)) {
      sharedPreferences.edit()
          .putBoolean(SettingKeys.USER_ONBOARDED, true)
          .apply()
      //need to attempt and get user's location first
      Dexter.withActivity(this)
          .withPermissions(
              permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION
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
              permissions: MutableList<PermissionRequest>?,
              token: PermissionToken?
            ) {
              token?.continuePermissionRequest()
            }

          })
          .check()
    } else {
      zipCodeObservable = rxSharedPreferences.getLong(SettingKeys.ZIP, ZipCodes.DEFAULT_ZIPCODE)
          .asObservable()
      zipCodeObservable?.let { observable ->
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
      getWeatherForZipLocation(it)
    } ?: run {
      //TODO: not found scenario, take them back to settings page? toast an Error?
    }
  }

  private fun setWeatherResponse(weatherResponse: WeatherResponse) {
    this.currentWeatherResponse = weatherResponse
    setupUI()
  }

  private fun getWeatherForZipLocation(zipLocation: ZipLocation) {
    loading_indicator.visibility = View.VISIBLE
    disposable.add(
        weatherApi.getWeather(zipLocation.latitude, zipLocation.longitude, currentTempUnit)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { response, throwable ->
              loading_indicator.visibility = View.GONE
              if (throwable != null) {
                if (throwable is HttpException) {
                  val responseBody = throwable.response()
                  Snackbar.make(
                      cl_parent,
                      getString(R.string.api_error, responseBody.code().toString()),
                      Snackbar.LENGTH_INDEFINITE
                  )
                      .setAction(
                          getString(R.string.retry)
                      ) { getWeatherForZipLocation(zipLocation) }

                      .setActionTextColor(
                          ContextCompat.getColor(this@HomeActivity, R.color.white)
                      )
                      .show()
                } else if (throwable is IOException) {
                  Snackbar.make(
                      cl_parent, getString(R.string.network_error),
                      Snackbar.LENGTH_INDEFINITE
                  )
                      .setAction(
                          getString(R.string.retry)
                      ) { getWeatherForZipLocation(zipLocation) }

                      .setActionTextColor(
                          ContextCompat.getColor(this@HomeActivity, R.color.white)
                      )
                      .show()
                }
              } else {
                setWeatherResponse(response)
              }
            })
  }

  private fun setupUI() {
    tv_location.visibility = View.VISIBLE
    tv_temp_subtitle.visibility = View.VISIBLE
    tv_temperature.visibility = View.VISIBLE
    tv_location.text =
        getString(R.string.city_state, currentZipLocation?.city, currentZipLocation?.state)
    val temp = currentWeatherResponse?.currentForecast?.temp
    //if (currentTempUnit == FAHRENHEIT && temp?:0.0 >= 60) {
    tv_temperature.text =
        if (currentWeatherResponse != null) Math.round(
            temp ?: 0.0
        ).toString()
        else
          getString(R.string.n_a)

    tv_temp_subtitle.text = currentWeatherResponse?.currentForecast?.summary?.capitalize()

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

    //savings the default zipcode
    //tODo show a list of nearby zip codes for the to select from
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
