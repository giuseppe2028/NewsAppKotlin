package com.example.newsappkotlin.View.Network

import com.example.newsappkotlin.View.Model.News
import com.example.newsappkotlin.View.Model.NewsSet
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val BASEURL = "https://newsapi.org/"
const val APIKEY = "d5e9ca056c3a4f72bf30dfcc6e14f545"

interface APINews {

    @GET("v2/top-headlines?category=business&apiKey=$APIKEY")
    //potrei anche aggiungere tutte le città, ma preferisco di no per ora
    fun getHeadNews(@Query("country") country:String):Call<NewsSet>

   // @GET("v2/everything?apiKey=$APIKEY")
    //fun getAllNews(@Query("q") argunent:String):Call<NewsSet>
   @GET("v2/everything?from=2023-06-21&sortBy=publishedAt&apiKey=$APIKEY")
   fun getAllNews(@Query("q") q:String) : Call<NewsSet>

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