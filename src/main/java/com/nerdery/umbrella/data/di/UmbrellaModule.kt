package com.nerdery.umbrella.data.di

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences.RxSharedPreferences
import com.google.gson.Gson
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.api.WeatherApi
import com.nerdery.umbrella.data.database.UmbrellaDatabase
import com.nerdery.umbrella.data.services.ApiServicesProvider
import com.nerdery.umbrella.data.services.IIconService
import com.nerdery.umbrella.data.services.ILocationService
import com.nerdery.umbrella.data.services.IZipCodeService
import com.nerdery.umbrella.data.services.impl.LocationService
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus

@Module
class UmbrellaModule(private val application: UmbrellaApp) {

  @Provides
  @UmbrellaScope
  fun providesDatabase(): UmbrellaDatabase {
    return UmbrellaDatabase.getAppDatabase(application)
  }

  @Provides
  @UmbrellaScope
  fun providesEventBus(): EventBus {
    return EventBus.getDefault() //let eb do it's singleton work
  }

  @Provides
  @UmbrellaScope
  fun providesApiServices(
    database: UmbrellaDatabase,
    sharedPreferences: SharedPreferences,
    eventBus: EventBus
  ): ApiServicesProvider {
    //TODO: decouple this provider into this module
    //Yes this is redundant for dagger's sake!
    return ApiServicesProvider(application, database, sharedPreferences, eventBus)
  }

  @Provides
  @UmbrellaScope
  fun providesPicasso(apiServicesProvider: ApiServicesProvider): Picasso {
    return apiServicesProvider.picasso
  }

  @Provides
  @UmbrellaScope
  fun providesGson(apiServicesProvider: ApiServicesProvider): Gson {
    return apiServicesProvider.gson
  }

  @Provides
  @UmbrellaScope
  fun providesSharedPreferences(): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(application)
  }

  @Provides
  @UmbrellaScope
  fun providesRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferences {
    return RxSharedPreferences.create(sharedPreferences)
  }

  @Provides
  @UmbrellaScope
  fun providesZipCodeService(apiServicesProvider: ApiServicesProvider): IZipCodeService {
    return apiServicesProvider.zipCodeService
  }

  @Provides
  @UmbrellaScope
  fun providesIconService(apiServicesProvider: ApiServicesProvider): IIconService {
    return apiServicesProvider.iconService
  }

  @Provides
  @UmbrellaScope
  fun providesWeatherService(apiServicesProvider: ApiServicesProvider): WeatherApi {
    return apiServicesProvider.weatherApi
  }

  @Provides
  @UmbrellaScope
  fun providesLocationService(eventBus: EventBus): ILocationService {
    return LocationService(application, eventBus)
  }

}