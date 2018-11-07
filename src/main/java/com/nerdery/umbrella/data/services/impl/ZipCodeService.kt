package com.nerdery.umbrella.data.services.impl

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import com.google.gson.Gson
import com.nerdery.umbrella.R.raw
import com.nerdery.umbrella.data.database.UmbrellaDatabase
import com.nerdery.umbrella.data.model.ZipLocation
import com.nerdery.umbrella.data.services.IZipCodeService
import com.nerdery.umbrella.data.services.IZipCodeService.FoundZipLocationsClosestToLocationEvent
import com.nerdery.umbrella.data.services.IZipCodeService.GetZipLocationByZipEvent
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.CoroutineStart.DEFAULT
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * This Service is used to lookup location details of a specific US ZIP code
 */
class ZipCodeService(
    private val database: UmbrellaDatabase,
    private val gson: Gson,
    private val eventBus: EventBus,
    private val sharedPreferences: SharedPreferences
) : IZipCodeService {

    companion object {
        const val PERCENTAGE_DIFFERENCE = .01 //TODO: not tested thoroughly....
    }

    override fun findZipLocationsClosestToLocation(location: Location?) {
        location?.let {
            GlobalScope.async(Dispatchers.Default, CoroutineStart.DEFAULT, null, {
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.CEILING
                val latMin = df.format(location.latitude * (1 - PERCENTAGE_DIFFERENCE))
                    .toDouble()
                val latMax = df.format(location.latitude * (1 + PERCENTAGE_DIFFERENCE))
                    .toDouble()
                val longMin = df.format(location.longitude * (1 - PERCENTAGE_DIFFERENCE))
                    .toDouble()
                val longMax = df.format(location.longitude * (1 + PERCENTAGE_DIFFERENCE))
                    .toDouble()
                //need to make sure the lower values are considered "min" because sqllite isn't smart enough I guess
                val acceptableZips = database.zipLocationDao()
                    .getZipLocationInRange(
                        Math.min(latMin, latMax), Math.max(latMin, latMax),
                        Math.min(longMin, longMax), Math.max(longMin, longMax)
                    )
                Timber.i("Found ${acceptableZips.count()} acceptable zips")
                eventBus.post(FoundZipLocationsClosestToLocationEvent(location, acceptableZips))
            })
        } ?: run {
            eventBus.post(FoundZipLocationsClosestToLocationEvent(location, null))
        }
    }

    override fun initDatabase(context: Context) {
        GlobalScope.async(Dispatchers.Default, DEFAULT, null, {
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
        })
    }

    /**
     * Request location details of a given zip code
     * @param context Generic context to retrieve zip codes resources
     * @param zipCode Numerical zip code
     * @param listener ZipLocationListener used to listen for successful result or error
     */
    override fun getZipLocationByZip(
        zipCode: Long
    ) {
        GlobalScope.async(Dispatchers.Default, DEFAULT, null, {
            val location = database.zipLocationDao()
                .all.firstOrNull { it.zipCode == zipCode }
            eventBus.post(GetZipLocationByZipEvent(location))
        })
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
