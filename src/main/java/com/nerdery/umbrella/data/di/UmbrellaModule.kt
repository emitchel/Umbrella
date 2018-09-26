package com.nerdery.umbrella.data.di

import android.preference.PreferenceManager
import com.f2prateek.rx.preferences.RxSharedPreferences
import com.google.gson.Gson
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.api.WeatherApi
import com.nerdery.umbrella.data.services.ApiServicesProvider
import com.nerdery.umbrella.data.services.IIconService
import com.nerdery.umbrella.data.services.IZipCodeService
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class UmbrellaModule(val application: UmbrellaApp) {

  @Provides
  @UmbrellaScope
  fun providesApiServices(application: UmbrellaApp): ApiServicesProvider {
    //Yes this is redundant for dagger's sake!
    return ApiServicesProvider(application)
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
  fun providesSharedPreferences(application: UmbrellaApp): RxSharedPreferences {
    val preferences = PreferenceManager.getDefaultSharedPreferences(application)
    return RxSharedPreferences.create(preferences)
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

}