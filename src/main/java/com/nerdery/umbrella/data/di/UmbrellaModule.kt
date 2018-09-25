package com.nerdery.umbrella.data.di

import com.google.gson.Gson
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.api.IconApi
import com.nerdery.umbrella.data.api.WeatherService
import com.nerdery.umbrella.data.services.ApiServicesProvider
import com.nerdery.umbrella.data.services.IconProvider
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class UmbrellaModule(val application: UmbrellaApp) {

  @Provides
  @UmbrellaScope
  fun providesIconProvider(): IconProvider {
    return IconProvider()
  }

  @Provides
  @UmbrellaScope
  fun providesApiServices(application: UmbrellaApp): ApiServicesProvider {
    //Yes this is redundant for dagger's sake!
    return ApiServicesProvider(application)
  }

  @Provides
  @UmbrellaScope
  fun providesIconApi(apiServicesProvider: ApiServicesProvider): IconApi {
    return apiServicesProvider.iconApi
  }

  @Provides
  @UmbrellaScope
  fun providesPicasso(apiServicesProvider: ApiServicesProvider): Picasso {
    return apiServicesProvider.picasso
  }

  @Provides
  @UmbrellaScope
  fun providesWeatherService(apiServicesProvider: ApiServicesProvider): WeatherService {
    return apiServicesProvider.weatherService
  }

  @Provides
  @UmbrellaScope
  fun providesGson(apiServicesProvider: ApiServicesProvider): Gson {
    return apiServicesProvider.gson
  }

}