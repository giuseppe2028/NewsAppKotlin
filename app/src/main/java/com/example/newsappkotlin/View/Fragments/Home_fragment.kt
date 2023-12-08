package com.example.newsappkotlin.View.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.location.LocationRequest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.newsappkotlin.R
import com.example.newsappkotlin.View.Adapter.HomeNewsAdapter
import com.example.newsappkotlin.View.DI.Controller.FirebaseController
import com.example.newsappkotlin.View.DI.model.LikedNews
import com.example.newsappkotlin.View.Model.News
import com.example.newsappkotlin.View.Model.NewsSet
import com.example.newsappkotlin.View.Model.User
import com.example.newsappkotlin.View.Network.ClientNetwork
import com.example.newsappkotlin.View.Network.ClientWeather
import com.example.newsappkotlin.databinding.FragmentHomeFragmentBinding

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [Home_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home_fragment : Fragment() {
    private  val PERMISSION_IF = 1000

    private lateinit var binding: FragmentHomeFragmentBinding
    private var param1: String? = null
    private var param2: String? = null
    lateinit var locationRequest:LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        //inizializzo le variabili:
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
        checkPermissions()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setWeather() {
        //faccio la richiesta http al server del meteo
        //TODO farlo con la posizione corrente
        ClientWeather.retrofit.getWeather(45.307497,18.435747).enqueue(
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
                    selectImageWeather((elemento as JsonObject)["text"].asString)


                }

                private fun selectImageWeather(tempo:String) {
                    val image = binding.imageView
                    when (tempo) {
                        "Mostly Clear" -> image.setImageResource(R.drawable.day)
                        "Mostly Sunny" -> image.setImageResource(R.drawable.day)
                        "Mostly Cloudy" -> image.setImageResource(R.drawable.cloudy)
                        "Sunny" -> image.setImageResource(R.drawable.day)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
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
                            topImageView.setOnClickListener {
                                //do implicit intent to URL profile
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                                startActivity(intent)
                            }

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
                                Log.i("debug", "dimensioneLista = ${news.listaArticles.size }")
                                adapter.lista = news.listaArticles
                                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                adapter.setOnClickListener(
                                    object :
                                HomeNewsAdapter.OnClickListener{
                                        override fun Onclick(position: Int, item: News) {
                                           val intent = Intent(Intent.ACTION_VIEW,Uri.parse(item.url))
                                            startActivity(intent)
                                        }

                                        override fun OnclickLike(position: Int, item: News) {
                                            if(adapter.like){
                                                FirebaseController.removeLike(User.id,item.url)

                                            }else{
                                                FirebaseController.addLike(LikedNews(item.url,item.urlToImage,item.title,User.id))

                                            }

                                        }

                                    }
                                )


                            }
                        }
                    else{

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
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun checkPermission():Boolean{
        if(ActivityCompat.checkSelfPermission(this.requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this.requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun RequestPermission(){

        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_IF,

        )
    }

    private fun isLocationEnabled():Boolean{
        var locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_IF){
            //usato solo per debug
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.i("ciao","Hai i permessi")
            }
        }
    }


    fun checkPermissions(){
        val requestPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
                isGaranted:Boolean->
            if(!isGaranted){
                binding.apply{
                    temperaturaattuale.visibility = View.GONE
                    linearLayout2.visibility = View.GONE
                    imageView.visibility = View.GONE
                }
            }
        }
    }
//TODO finire di fare i permessi per stampare la località
}
