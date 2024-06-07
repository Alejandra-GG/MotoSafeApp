package com.edwin.motosafe

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EventsScreenViewModel: ViewModel() {
    private val _accidents = MutableLiveData<List<Accident>>()
    val accidents: LiveData<List<Accident>> = _accidents

    init {
        fetchAccidents()
    }

    private fun fetchAccidents() {
        val db = Firebase.firestore
        db.collection("accidents")
            .get()
            .addOnSuccessListener { documents ->
                val accidentList = documents.map { document ->
                    Accident(
                        id = document.id,
                        longitude = document.getDouble("longitude") ?: 0.0,
                        latitude = document.getDouble("latitude") ?: 0.0,
                        accelerationX = document.getDouble("accelerationX") ?: 0.0,
                        accelerationY = document.getDouble("accelerationY") ?: 0.0,
                        accelerationZ = document.getDouble("accelerationZ") ?: 0.0
                    )
                }
                _accidents.value = accidentList
            }
            .addOnFailureListener { exception ->
                Log.e("EventsScreenViewModel", "Error getting documents: ${exception.message}", exception)
            }
    }
    fun logout(context: Context) {
        Firebase.auth.signOut()
        Toast.makeText(context, "Sesión cerrada exitosamente", Toast.LENGTH_LONG).show()
        // Aquí puedes agregar cualquier otra lógica necesaria después de cerrar sesión,
        // como navegar al inicio de sesión o limpiar datos.
    }

}