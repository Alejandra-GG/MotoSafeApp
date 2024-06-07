package com.edwin.motosafe

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenViewModel: ViewModel() {
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

    fun logout(context: Context) {
        Firebase.auth.signOut()
        Toast.makeText(context, "Sesión cerrada exitosamente", Toast.LENGTH_LONG).show()
        // Aquí puedes agregar cualquier otra lógica necesaria después de cerrar sesión,
        // como navegar al inicio de sesión o limpiar datos.
    }
}