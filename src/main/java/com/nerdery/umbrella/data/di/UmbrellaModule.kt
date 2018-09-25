package com.nerdery.umbrella.data.di

import com.google.gson.Gson
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.api.WeatherApi
import com.nerdery.umbrella.data.services.ApiServicesProvider
import com.nerdery.umbrella.data.services.IconService
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
  fun providesIconApi(apiServicesProvider: ApiServicesProvider): IconService {
    return apiServicesProvider.iconService
  }

  @Provides
  @UmbrellaScope
  fun providesPicasso(apiServicesProvider: ApiServicesProvider): Picasso {
    return apiServicesProvider.picasso
  }

  @Provides
  @UmbrellaScope
  fun providesWeatherService(apiServicesProvider: ApiServicesProvider): WeatherApi {
    return apiServicesProvider.weatherApi
  }

  @Provides
  @UmbrellaScope
  fun providesGson(apiServicesProvider: ApiServicesProvider): Gson {
    return apiServicesProvider.gson
  }

}