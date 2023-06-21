package com.example.newsappkotlin.View.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val BASEURL = "https://newsapi.org/"
val APIKEY = "ff1458b9ffdf49b4863451a7f8627695"

interface APINews {

    @GET("v2/top-headlines?country=us&category=business&apiKey=ff1458b9ffdf49b4863451a7f8627695")
    //potrei anche aggiungere tutte le città, ma preferisco di no per ora
    fun getHeadNews(@Query("country") country:String)

    @GET("v2/everything?from=2023-05-21&sortBy=publishedAt&apiKey=ff1458b9ffdf49b4863451a7f8627695")
    fun getAllNews(@Query("q") argunent:String)

}


object ClientNetwork{

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APINews::class.java)
    }


}