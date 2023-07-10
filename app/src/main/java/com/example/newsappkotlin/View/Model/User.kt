package com.example.newsappkotlin.View.Model

object  User{
    lateinit var mail:String
    lateinit var id:String
    fun setInformation(mail:String, id:String){
        this.mail = mail
        this.id = id
    }
}
