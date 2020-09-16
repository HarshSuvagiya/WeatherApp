package com.androdocs.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather?units=metric&")
    fun getCurrentWeatherData(@Query("q") lat: String, @Query("APPID") app_id: String): Call<WeatherResponse>
    @GET("data/2.5/weather?units=metric&")
    fun getCurrentWeatherData(@Query("lat") lat: String, @Query("lon") lon: String, @Query("APPID") app_id: String): Call<WeatherResponse>

}