package com.nerdery.umbrella.data.services.impl

import android.content.Context
import com.google.gson.Gson
import com.nerdery.umbrella.R
import com.nerdery.umbrella.data.model.ZipLocation
import com.nerdery.umbrella.data.services.IZipCodeService
import com.nerdery.umbrella.data.services.IZipCodeService.ZipLocationListener
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * This Service is used to lookup location details of a specific US ZIP code
 */
class ZipCodeService : IZipCodeService {

  /**
   * Request location details of a given zip code
   * @param context Generic context to retrieve zip codes resources
   * @param zipCode Numerical zip code
   * @param listener ZipLocationListener used to listen for successful result or error
   */
  override fun getLatLongByZip(
    context: Context,
    zipCode: String,
    listener: ZipLocationListener
  ) {
    var zipLong: Long = 0
    try {
      zipLong = java.lang.Long.valueOf(zipCode)
    } catch (e: NumberFormatException) {
      listener.onLocationNotFound()
    }

    //TODO: onload, move data to
    val stream = context.resources.openRawResource(R.raw.zipcodes)
    val jsonString = readJsonFile(stream)
    val gson = Gson()
    val locations = gson.fromJson(jsonString, Array<ZipLocation>::class.java)
    for (location in locations) {
      if (location.zipCode == zipLong) listener.onLocationFound(location)
    }
    listener.onLocationNotFound()
  }

  private fun readJsonFile(inputStream: InputStream): String? {
    val outputStream = ByteArrayOutputStream()
    val buffer = ByteArray(1024)
    var length: Int
    try {
      length = inputStream.read(buffer)
      while (length != -1) {
        outputStream.write(buffer, 0, length)
      }
    } catch (e: IOException) {
      e.printStackTrace()
      return null
    }

    return outputStream.toString()
  }

}
