package com.example.newsappkotlin.View.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappkotlin.View.Adapter.LikedNewsAdapter
import com.example.newsappkotlin.View.DI.Controller.FirebaseController
import com.example.newsappkotlin.View.DI.model.LikedNews
import com.example.newsappkotlin.databinding.FragmentLikeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LikeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LikeFragment : Fragment() {
    private lateinit var binding:FragmentLikeBinding
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
        binding = FragmentLikeBinding.inflate(layoutInflater)
        val adapter = LikedNewsAdapter(requireContext())
        getNews(adapter)
        adapter.setOnClickListener(object:LikedNewsAdapter.OnClickListener{
            override fun onClick(position: Int, likedNews: LikedNews) {
                FirebaseController.removeLike(likedNews.utente,likedNews.url)
                //riaggiorno la pagina:
                getNews(adapter)
            }

        })

        // Inflate the layout for this fragment
        return binding.root
    }
    fun getNews(adapter: LikedNewsAdapter) {
        FirebaseController.getNewsLiked {
                lista ->
            adapter.listaNews = lista
            binding.apply {
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
//requestDatabase


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LikeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LikeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}