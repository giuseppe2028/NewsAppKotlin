package com.example.newsappkotlin.View.Activities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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


         binding.navigationBar.setOnItemSelectedListener {

            selectItem->
            when(selectItem.itemId){
                R.id.news -> sostituisciFragment(Home_fragment())
                R.id.heart -> sostituisciFragment(LikeFragment())
                else -> {
                    true
                }
            }
        }

    }
    private fun sostituisciFragment(fragment:Fragment):Boolean{
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(binding.fragmentContainerHome.id,fragment).commit()
        return true
    }

    override fun onBackPressed() {
        Log.i("debug","niente")
    }

}