package com.nerdery.umbrella.data.services.impl

import android.content.Context
import com.google.gson.Gson
import com.nerdery.umbrella.R.raw
import com.nerdery.umbrella.data.database.UmbrellaDatabase
import com.nerdery.umbrella.data.model.ZipLocation
import com.nerdery.umbrella.data.services.IZipCodeService
import com.nerdery.umbrella.data.services.IZipCodeService.ZipLocationListener
import kotlinx.coroutines.experimental.async
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * This Service is used to lookup location details of a specific US ZIP code
 */
class ZipCodeService(
  private val database: UmbrellaDatabase,
  val gson: Gson
) : IZipCodeService {
  override fun initDatabase(context: Context) {
    async {
      Timber.i("Starting zip code init")
      val existingZipLocations = database.zipLocationDao()
          .all
      if (existingZipLocations == null || existingZipLocations.isEmpty()) {
        Timber.i("Zip locations didn't exist, loading from json now")
        val zipLocationsFromJson = getZipLocationsFromJson(context)
        zipLocationsFromJson?.let {
          Timber.i("Upserting %d zipLocations", it.size)
          database.zipLocationDao()
              .upsert(zipLocationsFromJson)
        } ?: run {
          Timber.e("Failed to load zip locations")
        }
      } else {
        Timber.i("Zip locations existed, %d of them", existingZipLocations.size)
      }
    }
  }

  /**
   * Request location details of a given zip code
   * @param context Generic context to retrieve zip codes resources
   * @param zipCode Numerical zip code
   * @param listener ZipLocationListener used to listen for successful result or error
   */
  override fun getLatLongByZip(
    zipCode: String,
    listener: ZipLocationListener
  ) {
    var zipLong: Long = 0
    try {
      zipLong = java.lang.Long.valueOf(zipCode)
    } catch (e: NumberFormatException) {
      listener.onLocationNotFound()
    }
    async {
      database.zipLocationDao()
          .all.first { it.zipCode == zipLong }?.let {
        listener.onLocationFound(it)
      } ?: run {
        listener.onLocationNotFound()
      }
    }
  }

  private fun getZipLocationsFromJson(context: Context): List<ZipLocation>? {
    val stream = context.resources.openRawResource(raw.zipcodes)
    val jsonString = readJsonFile(stream)
    val locations = gson.fromJson(jsonString, Array<ZipLocation>::class.java)
    return locations.toList()
  }

  private fun readJsonFile(inputStream: InputStream): String? {
    var ret = ""
    try {
      val inputStreamReader = InputStreamReader(inputStream)
      val bufferedReader = BufferedReader(inputStreamReader)
      val stringBuilder = StringBuilder()

      var receiveString = bufferedReader.readLine()
      while ((receiveString) != null) {
        stringBuilder.append(receiveString)
        receiveString = bufferedReader.readLine()
      }

      inputStream.close()
      ret = stringBuilder.toString()
    } catch (e: IOException) {
      Timber.e("Error loading json zip code, %s", e.printStackTrace());
      return null
    }

    return ret
  }

}
