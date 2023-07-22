package com.example.newsappkotlin.View.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsappkotlin.View.DI.model.LikedNews
import com.example.newsappkotlin.databinding.CardLikedNewsBinding

class LikedNewsAdapter(val context: Context):RecyclerView.Adapter<LikedNewsAdapter.ViewHolder>(){
    var listaNews = ArrayList<LikedNews>()
    class ViewHolder(binding:CardLikedNewsBinding):RecyclerView.ViewHolder(binding.root){
        val titolo = binding.title
        val immagine = binding.immagine
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardLikedNewsBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaNews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaNews[position]

        holder.titolo.text = item.title
        Glide.with(context).load(item.urlImage).into(holder.immagine)
    }

}
