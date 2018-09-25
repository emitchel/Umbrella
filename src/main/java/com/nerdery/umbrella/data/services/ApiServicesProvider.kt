package com.nerdery.umbrella.data.services

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES
import com.jakewharton.picasso.OkHttp3Downloader
import com.nerdery.umbrella.data.api.IconApi
import com.nerdery.umbrella.data.api.WeatherService
import com.nerdery.umbrella.data.serializers.DateDeserializer
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.Date

/**
 * Provides [Picasso], [WeatherService], and [IconApi]
 * that are all ready setup and ready to use.
 */
class ApiServicesProvider
/**
 * Constructor.
 *
 * @param application application context used for creating network caches.
 */
(application: Application) {

  /**
   * @return ready to use [IconApi]
   */
  val iconApi: IconApi
  /**
   * @return an instance of the [WeatherService] service that is ready to use.
   */
  val weatherService: WeatherService
  val picasso: Picasso
  val gson: Gson

  init {
    iconApi = IconApi()

    val gsonBuilder = GsonBuilder()
    gsonBuilder.registerTypeAdapter(Date::class.java, DateDeserializer())
    gson = gsonBuilder.create()

    val client = createOkHttpClient(application)

    weatherService =
        provideDarkSkyRetrofit(client, gson).create(WeatherService::class.java)

    picasso = Picasso.Builder(application)
        .downloader(OkHttp3Downloader(client))
        .listener { picasso, uri, e -> Timber.e(e, "Failed to load image: %s", uri) }
        .build()
  }

  private fun provideDarkSkyRetrofit(
    client: OkHttpClient,
    gson: Gson
  ): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.darksky.net/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
  }

  private fun createOkHttpClient(app: Application): OkHttpClient {
    // Install an HTTP cache in the application cache directory.
    val cacheDir = File(app.cacheDir, "http")
    val cache = Cache(cacheDir, DISK_CACHE_SIZE.toLong())

    return OkHttpClient.Builder()
        .cache(cache)
        .build()
  }

  companion object {

    private val DISK_CACHE_SIZE = MEGABYTES.toBytes(50)
        .toInt()
  }

}