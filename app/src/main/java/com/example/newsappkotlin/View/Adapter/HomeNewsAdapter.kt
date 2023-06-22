package com.example.newsappkotlin.View.Adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.newsappkotlin.View.Model.News

class HomeNewsAdapter(val lista:ArrayList<News>):RecyclerView.Adapter<HomeNewsAdapter.ViewHolder>() {

    class ViewHolder(val binding): RecyclerView.ViewHolder() {

    }
}