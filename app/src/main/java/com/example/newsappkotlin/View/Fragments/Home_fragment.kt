package com.example.newsappkotlin.View.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.newsappkotlin.View.Adapter.HomeNewsAdapter
import com.example.newsappkotlin.View.Model.NewsSet
import com.example.newsappkotlin.View.Network.ClientNetwork
import com.example.newsappkotlin.View.Network.ClientWeather
import com.example.newsappkotlin.databinding.FragmentHomeFragmentBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [Home_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home_fragment : Fragment() {
    private lateinit var binding: FragmentHomeFragmentBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeFragmentBinding.inflate(layoutInflater)
        hideElement()
        setWeather()
        setRecyclerView()
        setCard()
        showElement()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setWeather() {
        //faccio la richiesta http al server del meteo
        //TODO farlo con la posizione corrente
        ClientWeather.retrofit.getWeather(38.1156879,13.3612671).enqueue(
            object: Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    response.body().let {
                        if (it!=null){
                           showWeather(it)
                        }
                    }
                }

                private fun showWeather(jsonObject: JsonObject) {
                    val locationRequest = jsonObject.get("location") as JsonObject
                    val nomeCitta = locationRequest.get("city") //
                    val previsioni = jsonObject.get("forecasts") as JsonArray
                    //cerco di trovare il giorno corretto:
                    //TODO farlo con il giorno corrente
                    val listaFiltrata = previsioni.filter {
                            jsonElement ->
                        val elemento = jsonElement as JsonObject
                        elemento.get("day").asString == "Fri"
                    }
                    //mostro il primo elemento che sono sicuro che sia quello corrente
                    val elemento = listaFiltrata[0]
                    binding.apply {
                        //set location
                        location.text = nomeCitta.asString
                        val elemento = elemento as JsonObject
                        weather.text = elemento.get("text").asString
                        val high = elemento.get("high")
                        val low = elemento.get("low")
                        lowHighTemperature.text = "H:${high.asString}° L:${low.asString}°"
                        //dovrei mostrare la temperatura corrente ma siccome l'api non lo consente allora faccio la media
                        temperaturaattuale.text = ((high.asInt + low.asInt) / 2).toString()+"°"
                    }


                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            }
        )
    }

    private fun setCard() {
        ClientNetwork.retrofit.getHeadNews("us").enqueue(
            object: Callback<NewsSet>{
                override fun onResponse(call: Call<NewsSet>, response: Response<NewsSet>) {
                    val news = response.body()
                    if(news!=null){
                        binding.apply {
                            val item = news.listaArticles[0]
                            Log.i("ciao","${item.urlToImage}")
                            Glide.with(requireContext()).load(item.urlToImage).into(topImageView)
                            newsTitle.text = item.title
                        }
                    }
                }
                override fun onFailure(call: Call<NewsSet>, t: Throwable) {
                }

            }
        )
    }

    private fun setRecyclerView() {
        //make http request:
        val news = ClientNetwork.retrofit.getAllNews("apple")
        news.enqueue(
            object:Callback<NewsSet>{
                override fun onResponse(call: Call<NewsSet>, response: Response<NewsSet>) {
                    val news = response.body()
                        if (news != null) {
                            binding.apply {
                                val adapter  = HomeNewsAdapter(requireContext())
                                recyclerView.adapter = adapter
                                adapter.lista = news.listaArticles
                                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                            }
                        }
                }
                override fun onFailure(call: Call<NewsSet>, t: Throwable) {
                    Log.i("ciao","ciao2")
                }

            }
        )
    }

    private fun showElement() {
        binding.apply {
            baseLayout.visibility = View.VISIBLE
        }
    }

    private fun hideElement() {
        binding.apply {
            baseLayout.visibility = View.GONE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}