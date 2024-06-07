package com.edwin.motosafe

import android.content.Context
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavController, eventsScreenViewModel: EventsScreenViewModel = viewModel(), context: Context) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val accidents by eventsScreenViewModel.accidents.observeAsState(emptyList())

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
                            popUpTo("events") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Monitoreo") },
                    selected = false,
                    onClick = {
                        navController.navigate("monitoring") {
                            popUpTo("events") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Historial de eventos") },
                    selected = false,
                    onClick = {
                        navController.navigate("events") {
                            popUpTo("events") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        eventsScreenViewModel.logout(context)
                        navController.navigate("login") {
                            popUpTo("events") { inclusive = true }
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
                            Text(text = "Historial de eventos", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(15.dp))
                            // Mostrar la lista de accidentes
                            accidents.forEach { accident ->
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(text = "ID: ${accident.id}")
                                    Text(text = "Longitud: ${accident.longitude}")
                                    Text(text = "Latitud: ${accident.latitude}")
                                    Text(text = "Aceleración X: ${accident.accelerationX}")
                                    Text(text = "Aceleración Y: ${accident.accelerationY}")
                                    Text(text = "Aceleración Z: ${accident.accelerationZ}")
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
