package com.example.newsappkotlin.View.Model

import com.google.gson.annotations.SerializedName

//this is the rappresentation of arrays Json array
data class NewsSet (
    @SerializedName("articles")
    val listaArticles:ArrayList<News>,
    @SerializedName("status")
    val status:String,
    @SerializedName("totalResult")
    val totalResult:Int
        )