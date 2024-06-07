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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController, loginScreenViewModel: LoginScreenViewModel, context: Context){

    val blue_00a8e8 = colorResource(id = R.color.blue_00a8e8)
    val blue_007ea7 = colorResource(id = R.color.blue_007ea7)

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

// Estado de desplazamiento para el scroll
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Text(text = "MotoSafe",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(15.dp))

        Image(painter = painterResource(id = R.drawable.icon_app_circle),
            contentDescription = "Icono MotoSafe",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(100.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = { Text("Correo electronico") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = blue_00a8e8,
                focusedLabelColor = blue_00a8e8,
                unfocusedBorderColor = blue_00a8e8,
                cursorColor = blue_00a8e8),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Contrasena") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = blue_00a8e8,
                focusedLabelColor = blue_00a8e8,
                unfocusedBorderColor = blue_00a8e8,
                cursorColor = blue_00a8e8),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        val isEmailValid = isValidEmail(email)
        val isFormValid = email.isNotEmpty() && password.isNotEmpty() && isEmailValid

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { navController.navigate("signin"){
                popUpTo("login") { inclusive = true }
            } }, colors = ButtonDefaults.buttonColors(blue_007ea7)) {
                Text(text = "Registrarse")
            }


            Button(onClick = { if (isFormValid) {
                                    loginScreenViewModel.signIn(email, password, context) {

                                        navController.navigate("home"){
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                }
            },
                enabled = isFormValid, // El botón estará habilitado solo si el formulario es válido
                colors = ButtonDefaults.buttonColors(blue_00a8e8)
            ) {
                Text(text = "Ingresar")
            }
        }
    }
}

