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

class SigninScreenViewModel: ViewModel() {
    private val db = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signUp(context: Context, email: String, password: String, gender: String, name: String, address: String, bloodType: String, age: Int, height: Double, weight: Double, allergies: String, home: () -> Unit){
        if (_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{it ->
                if (it.isSuccessful){
                    val user = hashMapOf(
                        "name" to name,
                        "age" to age,
                        "address" to address,
                        "gender" to gender,
                        "height" to height,
                        "weight" to weight,
                        "bloodType" to bloodType,
                        "allergies" to allergies,
                        "email" to email
                        )

                    // Add a new document with a generated ID
                    db.collection("users")
                        .document(email)
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error de registro", Toast.LENGTH_LONG).show()
                        }


                    home()
                }else{
                    Log.d("SigUp", "${it.result}")
                }
                _loading.value = false
            }
        }
        home()
    }
}