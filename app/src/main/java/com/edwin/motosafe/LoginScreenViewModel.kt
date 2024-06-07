package com.edwin.motosafe

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    fun signIn(email: String, password: String, context: Context, home: () -> Unit) = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{it ->
                if (it.isSuccessful){
                    Toast.makeText(context, "Ingresaste con exito", Toast.LENGTH_LONG).show()
                    home()
                }else{
                    Toast.makeText(context, "Datos incorrectos", Toast.LENGTH_LONG).show()
                }
            }
        }catch (ex: Exception){
            Log.d ("Login", "${ex.message}")
        }
    }

}