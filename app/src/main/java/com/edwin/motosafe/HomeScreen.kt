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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, homeScreenViewModel: HomeScreenViewModel, context: Context){
    val user by homeScreenViewModel.userDetails.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val blue_e6f8ff = colorResource(id = R.color.blue_e6f8ff)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = "MotoSafe",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(15.dp))

                    Image(painter = painterResource(id = R.drawable.icon_app_circle),
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
                        navController.navigate("home"){
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Monitoreo") },
                    selected = false,
                    onClick = {
                        navController.navigate("monitoring"){
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Historial de eventos") },
                    selected = false,
                    onClick = {
                        navController.navigate("events"){
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        homeScreenViewModel.logout(context)
                        navController.navigate("login"){
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }, gesturesEnabled = true
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
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    containerColor = blue_e6f8ff,
                    icon = { Icon(Icons.Filled.Edit, "Editar usuario") },
                    text = { Text(text = "Editar") },
                    onClick = {
                        navController.navigate("editprofile"){
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        ) { contentPadding ->
            homeScreenViewModel.consult(context) {
                navController.navigate("home"){
                    popUpTo("home") { inclusive = true }
                }
            }
            Surface(modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(24.dp)
                .fillMaxSize()) {
                Column (modifier = Modifier.fillMaxSize()){
                    Row (modifier = Modifier.fillMaxWidth()){
                        Column(modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val imagePerfil = if (user.gender == "Masculino") R.drawable.avatar_male else R.drawable.avatar_female
                            Image(
                                painter = painterResource(id = imagePerfil),
                                contentDescription = "Imagen",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = "Hola, ${user.name}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))


                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start){
                            Text(
                                text = "Datos de Perfil",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = "Edad: ${user.age} años",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Dirección: ${user.address}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Tipo de Sangre: ${user.bloodType}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Altura: ${user.height} mts.",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Peso: ${user.weight} kg.",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Genero: ${user.gender}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Alergias: ${user.allergies}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Correo electrónico: ${user.email}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}
