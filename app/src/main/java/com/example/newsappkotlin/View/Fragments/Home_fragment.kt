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
import com.example.newsappkotlin.databinding.FragmentHomeFragmentBinding
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
        setPage()
        setCard()
        showElement()
        // Inflate the layout for this fragment
        return binding.root
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

    private fun setPage() {
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