package com.edwin.motosafe

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditProfileScreenViewModel: ViewModel() {

    private val _userDetails = MutableStateFlow(User())
    val userDetails: StateFlow<User> = _userDetails.asStateFlow()
    fun consult(context: Context, home: () -> Unit){
        val db = Firebase.firestore
        val userId = Firebase.auth.currentUser?.email
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        _userDetails.value = document.toObject(User::class.java) ?: User()
                    } else {
                        Toast.makeText(context, "No se encontró el documento", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error al obtener informacion", Toast.LENGTH_LONG).show()
                }
        }
    }

    private val db = Firebase.firestore

    fun signUp(context: Context, email: String, gender: String, name: String, address: String, bloodType: String, age: Int, height: Double, weight: Double, allergies: String, home: () -> Unit) {
        val user = mapOf(
            "name" to name,
            "age" to age,
            "address" to address,
            "gender" to gender,
            "height" to height,
            "weight" to weight,
            "bloodType" to bloodType,
            "allergies" to allergies
        )

        // Add a new document with a generated ID
        db.collection("users")
            .document(email)
            .update(user)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error de registro", Toast.LENGTH_LONG).show()
            }
        home()
    }
}