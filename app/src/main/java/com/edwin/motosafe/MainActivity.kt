package com.edwin.motosafe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth


val user = FirebaseAuth.getInstance().currentUser
class MainActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (user != null) {
                // Iniciar el servicio de monitoreo de Firebase
                val serviceIntent = Intent(this, AccelerationMonitoringService::class.java)
                startService(serviceIntent)

                // Solicitar permiso para enviar SMS si no está concedido
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), PERMISSION_REQUEST_CODE)
                }
            }
            Windows()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permiso concedido
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso para enviar SMS denegado.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
@Composable
fun Windows() {
    val navController = rememberNavController()
    val loginScreenViewModel = viewModel<LoginScreenViewModel>()
    val signinScreenViewModel = viewModel<SigninScreenViewModel>()
    val homeScreenViewModel = viewModel<HomeScreenViewModel>()
    val monitoringScreenViewModel = viewModel<MonitoringScreenViewModel>()
    val editProfileScreenViewModel = viewModel<EditProfileScreenViewModel>()
    val eventsScreenViewModel = viewModel<EventsScreenViewModel>()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = if (user != null) "home" else "login") {
        composable("login") { LoginScreen(navController, loginScreenViewModel, context) }
        composable("signin") { SigninScreen(navController, signinScreenViewModel, context) }
        composable("home") { HomeScreen(navController, homeScreenViewModel, context) }
        composable("editprofile") { EditProfileScreen(navController, editProfileScreenViewModel, context) }
        composable("monitoring") { MonitoringScreen(navController, monitoringScreenViewModel, context) }
        composable("events") { EventsScreen(navController, eventsScreenViewModel, context) }
        // Define otras rutas y pantallas aquí
    }
}