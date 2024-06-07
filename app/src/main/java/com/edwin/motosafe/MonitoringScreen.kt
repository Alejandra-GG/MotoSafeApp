package com.edwin.motosafe

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonitoringScreen(navController: NavController, monitoringScreenViewModel: MonitoringScreenViewModel = viewModel(), context: Context) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val blue_e6f8ff = colorResource(id = R.color.blue_e6f8ff)
    val accelerationData by monitoringScreenViewModel.accelerationData.observeAsState()
    val locationData by monitoringScreenViewModel.locationData.observeAsState()

    // Inicialmente asignar la posición a un lugar por defecto
    val singapore = LatLng(1.3521, 103.8198) // Coordenadas de Singapur

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    // Leer los datos al componer el Composable
    LaunchedEffect(Unit) {
        monitoringScreenViewModel.readAccelerationData(context)
        monitoringScreenViewModel.readLocationData(context)
    }

    // Actualizar la posición de la cámara cuando los datos de ubicación estén disponibles
    LaunchedEffect(locationData) {
        locationData?.let {
            val accidentPosition = LatLng(it.latitude, it.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(accidentPosition, 15f)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "MotoSafe",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Image(
                        painter = painterResource(id = R.drawable.icon_app_circle),
                        contentDescription = "Icono MotoSafe",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                    )
                }
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Datos de Perfil") },
                    selected = false,
                    onClick = {
                        navController.navigate("home") {
                            popUpTo("monitoring") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Monitoreo") },
                    selected = false,
                    onClick = {
                        navController.navigate("monitoring") {
                            popUpTo("monitoring") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Historial de eventos") },
                    selected = false,
                    onClick = {
                        navController.navigate("events") {
                            popUpTo("monitoring") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        monitoringScreenViewModel.logout(context)
                        navController.navigate("login") {
                            popUpTo("monitoring") { inclusive = true }
                        }
                    }
                )
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("MotoSafe") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Abrir Menú")
                        }
                    }
                )
            }
        ) { contentPadding ->
            Surface(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding)
                    .padding(24.dp)
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Ubicación", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(15.dp))
                            GoogleMap(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(500.dp),
                                cameraPositionState = cameraPositionState
                            ) {
                                locationData?.let {
                                    Marker(
                                        state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                                        title = "Accident Location",
                                        snippet = "Marker at accident location"
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(text = "Datos del Acelerómetro", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(15.dp))
                            accelerationData?.let {
                                Text(text = "Valor de la posición x: ${it.x}")
                                Spacer(modifier = Modifier.height(15.dp))
                                Text(text = "Valor de la posición y: ${it.y}")
                                Spacer(modifier = Modifier.height(15.dp))
                                Text(text = "Valor de la posición z: ${it.z}")
                            } ?: run {
                                Text(text = "Cargando datos del acelerómetro...")
                            }
                        }
                    }
                }
            }
        }
    }
}
