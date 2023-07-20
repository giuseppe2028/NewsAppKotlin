package com.example.newsappkotlin.View.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsappkotlin.R
import com.example.newsappkotlin.View.Fragments.Home_fragment
import com.example.newsappkotlin.View.Model.News
import com.example.newsappkotlin.databinding.CardHomeBinding

class HomeNewsAdapter(val context: Context):RecyclerView.Adapter<HomeNewsAdapter.ViewHolder>() {
    private var setOnClickListener: OnClickListener? = null
    interface OnClickListener{
        fun Onclick(position: Int,item:News)
        fun OnclickLike(position: Int,item: News)
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        this.setOnClickListener = onClickListener
    }
    var lista = ArrayList<News>()
    class ViewHolder(binding: CardHomeBinding): RecyclerView.ViewHolder(binding.root) {
        val imageNotizia = binding.imageView2
        val title = binding.title
        val bottoneLike = binding.likeButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = CardHomeBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        //setto l'immagine
        Glide.with(context).load(item.urlToImage).into(holder.imageNotizia)
        holder.title.text = item.title
        holder.itemView.setOnClickListener {
            setOnClickListener?.Onclick(position,item)
        }
        holder.bottoneLike.setOnClickListener {
            setOnClickListener?.OnclickLike(position,item)
            val imageOfButton = holder.bottoneLike.drawable
            //faccio l'interrogazione al DB per settare l'immagine

            holder.bottoneLike.setImageResource(R.drawable.heart_filled)
        }

    }
}