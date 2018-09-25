package com.nerdery.umbrella.data.api;

import com.nerdery.umbrella.BuildConfig;
import com.nerdery.umbrella.data.model.WeatherResponse;
import com.nerdery.umbrella.data.model.TempUnit;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("/forecast/" + BuildConfig.API_KEY + "/{latitude},{longitude}")
    Single<WeatherResponse> getWeather(@Path("latitude") double latitude,
                                       @Path("longitude") double longitude,
                                       @Query("units") TempUnit units);

    @GET("/forecast/" + BuildConfig.API_KEY + "/{latitude},{longitude}")
    Call<WeatherResponse> getWeatherCall(@Path("latitude") double latitude,
                                         @Path("longitude") double longitude,
                                         @Query("units") TempUnit units);

}
