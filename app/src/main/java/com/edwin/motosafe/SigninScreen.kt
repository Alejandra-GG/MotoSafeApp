package com.edwin.motosafe

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun SigninScreen(navController: NavController, signinScreenViewModel: SigninScreenViewModel, context: Context){
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedBlood by remember { mutableStateOf("") }
    var selectedAge by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    val blue_00a8e8 = colorResource(id = R.color.blue_00a8e8)
    val blue_007ea7 = colorResource(id = R.color.blue_007ea7)


    Surface(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            GenderSelection(gender) { gender = it }
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {name = it},
                label = { Text("Nombre") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue_007ea7,
                    focusedLabelColor = blue_007ea7,
                    unfocusedBorderColor = blue_007ea7,
                    cursorColor = blue_007ea7),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = address,
                onValueChange = {address = it},
                label = { Text("Dirección") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue_007ea7,
                    focusedLabelColor = blue_007ea7,
                    unfocusedBorderColor = blue_007ea7,
                    cursorColor = blue_007ea7),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

                Column(modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)) {
                    DropdownMenuBloodType(selectedBlood) { newBloodType ->
                        selectedBlood = newBloodType
                    }
                }

                Column (modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)) {
                    DropdownMenuAge(selectedAge) { newAge ->
                        selectedAge = newAge
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            // Campos para Altura y Peso

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = height,
                onValueChange = {height = it},
                label = { Text("Altura en metros") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue_007ea7,
                    focusedLabelColor = blue_007ea7,
                    unfocusedBorderColor = blue_007ea7,
                    cursorColor = blue_007ea7),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = weight,
                onValueChange = {weight = it},
                label = { Text("Peso en kilogramos") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue_007ea7,
                    focusedLabelColor = blue_007ea7,
                    unfocusedBorderColor = blue_007ea7,
                    cursorColor = blue_007ea7),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                value = allergies,
                onValueChange = {allergies = it},
                label = { Text("Alergias") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue_007ea7,
                    focusedLabelColor = blue_007ea7,
                    unfocusedBorderColor = blue_007ea7,
                    cursorColor = blue_007ea7),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {email = it},
                label = { Text("Correo electrónico") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue_007ea7,
                    focusedLabelColor = blue_007ea7,
                    unfocusedBorderColor = blue_007ea7,
                    cursorColor = blue_007ea7),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = {password = it},
                label = { Text("Contraseña") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue_007ea7,
                    focusedLabelColor = blue_007ea7,
                    unfocusedBorderColor = blue_007ea7,
                    cursorColor = blue_007ea7),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(10.dp))

            val isEmailValid = isValidEmail(email)
            val isPasswordValid = password.length >= 6
            val isFormValid = email.isNotEmpty() &&
                    password.isNotEmpty() &&
                    name.isNotEmpty() &&
                    address.isNotEmpty() &&
                    selectedBlood.isNotEmpty() &&
                    selectedAge.isNotEmpty() &&
                    height.isNotEmpty() &&
                    weight.isNotEmpty() &&
                    allergies.isNotEmpty() &&
                    gender.isNotEmpty() &&
                    isEmailValid &&
                    isPasswordValid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = { navController.navigate("login"){
                    popUpTo("signin") { inclusive = true }
                } }, colors = ButtonDefaults.buttonColors(blue_007ea7)) {
                    Text(text = "Cancelar")
                }


                Button(onClick = {
                    signinScreenViewModel.signUp(context, email, password, gender, name, address, selectedBlood, selectedAge.toInt(), height.toDouble(), weight.toDouble(), allergies) {
                        navController.navigate("home"){
                            popUpTo("signin") { inclusive = true }
                        }
                    }
                },
                    enabled = isFormValid, // El botón estará habilitado solo si el formulario es válido
                    colors = ButtonDefaults.buttonColors(blue_00a8e8)
                ) {
                    Text(text = "Confirmar")
                }
            }
        }

    }
}