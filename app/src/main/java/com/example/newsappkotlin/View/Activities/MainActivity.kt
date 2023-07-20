package com.example.newsappkotlin.View.Activities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.newsappkotlin.R
import com.example.newsappkotlin.View.Fragments.Home_fragment
import com.example.newsappkotlin.View.Fragments.LikeFragment
import com.example.newsappkotlin.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navSelector()
    }

    private fun navSelector(){
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val selectItem = binding.navigationBar.selectedItemId
        when(selectItem){
            R.id.news ->
                transaction.replace(binding.fragmentContainerHome.id,Home_fragment())

            R.id.heart -> transaction.replace(binding.fragmentContainerHome.id,LikeFragment())
        }
    }

    override fun onBackPressed() {
        Log.i("debug","niente")
    }

}