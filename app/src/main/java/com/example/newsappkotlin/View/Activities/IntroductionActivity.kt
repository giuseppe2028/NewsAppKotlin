package com.example.newsappkotlin.View.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsappkotlin.R
import com.example.newsappkotlin.databinding.ActivityIntroductionBinding

class IntroductionActivity : AppCompatActivity() {
    private lateinit var binding:ActivityIntroductionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}