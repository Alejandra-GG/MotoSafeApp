package com.edwin.motosafe

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore

class AccelerationMonitoringService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        readAccelerationData(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cleanup resources if needed
    }

    private fun readAccelerationData(context: Context) {
        val database = Firebase.database
        val accelerationRef = database.getReference("acceleration")
        accelerationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val acceleration = snapshot.getValue(Acceleration::class.java)
                if (acceleration != null) {
                    evaluateAndSendNotification(context, acceleration)
                } else {
                    Toast.makeText(
                        context,
                        "No se encontraron datos de aceleración en la base de datos.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to read acceleration data.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun evaluateAndSendNotification(context: Context, acceleration: Acceleration) {
        if (acceleration.x < 0.0 && acceleration.y < 0.0) {
            val database = FirebaseDatabase.getInstance()
            val locationRef = database.getReference("location")
            locationRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val location = snapshot.getValue(Location::class.java)
                    if (location != null) {
                        val db = Firebase.firestore
                        val userId = Firebase.auth.currentUser?.email
                        if (userId != null) {
                            db.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val informacion = document.data
                                        val info: Map<String, String?> = mapOf(
                                            "latitude" to "${location.latitude}",
                                            "longitude" to "${location.longitude}",
                                            "name" to "${informacion?.get("name")}",
                                            "age" to "${informacion?.get("age")}",
                                            "address" to "${informacion?.get("address")}",
                                            "bloodType" to "${informacion?.get("bloodType")}",
                                            "height" to "${informacion?.get("height")}",
                                            "weight" to "${informacion?.get("weight")}",
                                            "gender" to "${informacion?.get("gender")}",
                                            "allergies" to "${informacion?.get("allergies")}"
                                        )


                                        db.collection("accidents")
                                            .add(mapOf(
                                                "longitude" to location.longitude,
                                                "latitude" to location.latitude,
                                                "accelerationX" to acceleration.x,
                                                "accelerationY" to acceleration.y,
                                                "accelerationZ" to acceleration.z
                                            ))
                                            .addOnSuccessListener { documentReference ->
                                                val accidentId = "accidente${documentReference.id}"
                                                sendNotification(context)
                                                sendSmsEme(context, "+527861403632", info)
                                                sendSmsFam(context, "+527861485107", info)
                                            }
                                            .addOnFailureListener { exception ->
                                                Toast.makeText(context, "Error al guardar datos del accidente", Toast.LENGTH_LONG).show()
                                            }

                                    } else {
                                        Toast.makeText(context, "No se encontró el documento", Toast.LENGTH_LONG).show()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(context, "Error al obtener información", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        Toast.makeText(context, "No se encontraron datos de ubicación en la base de datos.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to read location data.", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
