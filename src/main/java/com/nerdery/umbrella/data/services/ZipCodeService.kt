package com.nerdery.umbrella.data.services

import android.content.Context

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.nerdery.umbrella.R

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * This Service is used to lookup location details of a specific US ZIP code
 */
class ZipCodeService {

  interface ZipLocationListener {
    fun onLocationFound(location: ZipLocation)
    fun onLocationNotFound()
  }

  inner class ZipLocation {
    @SerializedName("z")
    var zipCode: Long = 0
      internal set
    @SerializedName("c")
    var city: String? = null
      internal set
    @SerializedName("s")
    var state: String? = null
      internal set
    @SerializedName("la")
    var latitude: Double = 0.toDouble()
      internal set
    @SerializedName("lo")
    var longitude: Double = 0.toDouble()
      internal set
  }

  companion object {

    /**
     * Request location details of a given zip code
     * @param context Generic context to retrieve zip codes resources
     * @param zipCode Numerical zip code
     * @param listener ZipLocationListener used to listen for successful result or error
     */
    fun getLatLongByZip(
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

}
