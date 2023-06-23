package com.example.newsappkotlin.View.Network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIWheater {
    @Headers(
        "X-RapidAPI-Key: 0aa7b9bbc5msh54cd129a1f102bcp15f31ejsnfcd17838d6c1",
        "X-RapidAPI-Host: yahoo-weather5.p.rapidapi.com"
    )
    @GET("weather")
    fun getWeather(
        @Query("lat") latitude: Double,
        @Query("long") longitude: Double,
        @Query("format") format: String = "json",
        @Query("u") unit: String = "c"
    ): Call<JsonObject>
}

object ClientWeather{
    val retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl("https://yahoo-weather5.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIWheater::class.java)
    }
}