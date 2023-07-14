package com.example.newsappkotlin.View.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsappkotlin.R
import com.example.newsappkotlin.View.Activities.MainActivity
import com.example.newsappkotlin.View.Model.User
import com.example.newsappkotlin.databinding.FragmentLoginBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    val firedatabase = FirebaseFirestore.getInstance()
    private lateinit var binding:FragmentLoginBinding
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
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        clickManager()


        return binding.root

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }





    private fun clickManager() {
        binding.apply {
            bottoneLogin.setOnClickListener {
                val mailText = mail.text.toString()
                val passwordText = password.text.toString()
                checkValues(mailText,passwordText)
            }
            registrati.setOnClickListener {
                registrati()
            }
        }
    }

    private fun checkValues(mail: String, password: String) {
        Log.i("Query", "$mail $password")

        firedatabase.collection("User")
            .whereEqualTo("mail", mail)
            .whereEqualTo("password", password)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful()) {
                    val querySnapshot = task.getResult()
                    if (!querySnapshot.isEmpty()) {
                        acceptLogin(querySnapshot,mail)
                    }
                else {
                    Log.i("Query", "utente non loggato")
                    }
                }
            else {
                // Si è verificato un errore durante l'esecuzione della query
                // Gestisci l'errore o visualizza un messaggio di errore appropriato
            }
            }
    }
    private fun acceptLogin(querySnapshot: QuerySnapshot,mail:String) {
        for (document in querySnapshot.documents) {
            val userId = document.id
            User.setInformation(userId,mail)
            changeScreen()
        }

    }

    private fun changeScreen() {
        val i = Intent(this.context,MainActivity::class.java)
        startActivity(i)
    }
    fun registrati(){

            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView,RegisterFragment()).commit()

    }
}