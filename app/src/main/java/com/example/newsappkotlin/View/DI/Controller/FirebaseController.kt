package com.example.newsappkotlin.View.DI.Controller

import android.util.Log
import com.example.newsappkotlin.View.DI.model.LikedNews
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseController {
    val TAG = "debug"
    val db = FirebaseFirestore.getInstance()
    private var counter: Int = 1;
    fun register(oggetto: Any) {
        db.collection("User").document("user" + counter).set(oggetto)
            .addOnSuccessListener {
                // Operazione completata con successo
                Log.d(TAG, "Dati inseriti correttamente")
            }
            .addOnFailureListener { e ->
                // Errore durante l'inserimento dei dati
                Log.w(TAG, "Errore durante l'inserimento dei dati", e)
            }

        counter++
    }


    fun addLike(news:LikedNews) {
        db.collection("Like").add(news)
            .addOnSuccessListener {
                // Operazione completata con successo
                Log.d(TAG, "Dati inseriti correttamente")
            }
            .addOnFailureListener { e ->
                // Errore durante l'inserimento dei dati
                Log.w(TAG, "Errore durante l'inserimento dei dati", e)
            }


    }
     fun removeLike(user:String,url:String){
        val document = db.collection("Like")
        val query = document.whereEqualTo("url",url).whereEqualTo("utente",user)
        Log.i("ciao","sonoquiii")
        query.get()
            .addOnSuccessListener { querySnapshot ->
                for (documentSnapshot in querySnapshot) {

                    // Delete each document that matches the condition
                    documentSnapshot.reference.delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Document deleted successfully.")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error deleting document.", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents for deletion.", e)
            }
    }

    fun getNewsLiked(callback: (ArrayList<LikedNews>) -> Unit) {
        val lista: ArrayList<LikedNews> = ArrayList()
        val document = db.collection("Like")
        document.get().addOnSuccessListener { querySnapshot ->
            for (documento in querySnapshot) {
                val url = documento.getString("url").toString()
                val urlImage = documento.getString("urlImage").toString()
                val utente = documento.getString("utente").toString()
                val titolo = documento.getString("title").toString()
                lista.add(LikedNews(url, urlImage, titolo, utente))
            }
            // Call the callback with the populated list when data retrieval is complete
            callback(lista)
        }
    }


}