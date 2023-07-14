package com.example.newsappkotlin.View.DI.Controller

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseController {
    val TAG ="debug"
    val db = FirebaseFirestore.getInstance()
    private var counter:Int = 1;
    fun register(oggetto:Any){
        db.collection("User").document("user"+ counter).set(oggetto)
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
}