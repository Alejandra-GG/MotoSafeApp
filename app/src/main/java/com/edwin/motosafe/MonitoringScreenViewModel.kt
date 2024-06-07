package com.edwin.motosafe

import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.database.DatabaseError
import com.google.firebase.ktx.Firebase

class MonitoringScreenViewModel : ViewModel() {

    private val _accelerationData = MutableLiveData<Acceleration>()
    val accelerationData: LiveData<Acceleration> get() = _accelerationData

    private val _locationData = MutableLiveData<Location>()
    val locationData: LiveData<Location> get() = _locationData

    fun readAccelerationData(context: Context) {
        val database = Firebase.database
        val accelerationRef = database.getReference("acceleration")
        accelerationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val acceleration = snapshot.getValue(Acceleration::class.java)
                _accelerationData.value = acceleration ?: throw IllegalStateException("Acceleration data is null")
                if (acceleration == null) {
                    Toast.makeText(context, "No se encontraron datos de aceleración en la base de datos.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to read acceleration data.", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun readLocationData(context: Context) {
        val database = Firebase.database
        val locationRef = database.getReference("location")
        locationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val location = snapshot.getValue(Location::class.java)
                _locationData.value = location ?: throw IllegalStateException("Location data is null")
                if (location == null) {
                    Toast.makeText(context, "No se encontraron datos de ubicación en la base de datos.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to read location data.", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun logout(context: Context) {
        Firebase.auth.signOut()
        Toast.makeText(context, "Sesión cerrada exitosamente", Toast.LENGTH_LONG).show()
    }
}