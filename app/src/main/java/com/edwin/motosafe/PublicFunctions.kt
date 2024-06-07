package com.edwin.motosafe

import android.app.NotificationManager
import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

@Composable
fun GenderSelection(selectedGender: String, onGenderSelected: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly,  verticalAlignment = Alignment.CenterVertically) {
        GenderOption("Masculino", selectedGender == "Masculino", onGenderSelected, R.drawable.avatar_male)
        Spacer(modifier = Modifier.width(16.dp))
        GenderOption("Femenino", selectedGender == "Femenino", onGenderSelected, R.drawable.avatar_female)
    }
}

@Composable
fun GenderOption(gender: String, isSelected: Boolean, onGenderSelected: (String) -> Unit, imageName: Int) {
    val blue_007ea7 = colorResource(id = R.color.blue_007ea7)
    Column (modifier = Modifier
        .clip(RectangleShape)
        .border(1.dp, color = blue_007ea7, shape = RoundedCornerShape(5.dp))
        .padding(10.dp)
        .clickable {
            onGenderSelected(gender)
        }, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = imageName),
                contentDescription = "Icono MotoSafe",
                modifier = Modifier.width(((LocalConfiguration.current.screenWidthDp.dp)/2)-52.dp))
        }
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            RadioButton(colors = RadioButtonDefaults.colors(blue_007ea7), selected = isSelected, onClick = { onGenderSelected(gender) })
            Text(text = gender)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBloodType(selectedBlood: String, onBloodSelected: (String) -> Unit) {
    val blue_007ea7 = colorResource(id = R.color.blue_007ea7)
    val blue_e6f8ff = colorResource(id = R.color.blue_e6f8ff)

    val bloodTypes = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    var isExpanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = !isExpanded }) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                value = selectedBlood,
                onValueChange = { /* No se usa en modo de solo lectura */ },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                label = { Text("Tipo de Sangre") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue_007ea7,
                    focusedLabelColor = blue_007ea7,
                    unfocusedBorderColor = blue_007ea7,
                    cursorColor = blue_007ea7
                )
            )

            ExposedDropdownMenu(modifier = Modifier.background(blue_e6f8ff), expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                bloodTypes.forEach { bloodType ->
                    DropdownMenuItem(
                        text = { Text(text = bloodType) },
                        onClick = {
                            onBloodSelected(bloodType)
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuAge(selectedAge: String, onAgeSelected: (String) -> Unit) {
    val blue_007ea7 = colorResource(id = R.color.blue_007ea7)
    val blue_e6f8ff = colorResource(id = R.color.blue_e6f8ff)

    val ages = (18..99).toList()
    var isExpanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = !isExpanded }) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                value = selectedAge,
                onValueChange = { /* No se usa en modo de solo lectura */ },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                label = { Text("Edad") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blue_007ea7,
                    focusedLabelColor = blue_007ea7,
                    unfocusedBorderColor = blue_007ea7,
                    cursorColor = blue_007ea7
                )
            )

            ExposedDropdownMenu(modifier = Modifier.background(blue_e6f8ff), expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                ages.forEach { age ->
                    DropdownMenuItem(
                        text = { Text(text = age.toString()) },
                        onClick = {
                            onAgeSelected(age.toString())
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

fun sendNotification(context: Context) {
    val notificationManager = NotificationManagerCompat.from(context)
    val notification = NotificationCompat.Builder(context, MyApp.CHANNEL_ID)
        .setContentTitle("URGENTE")
        .setContentText("Se ha detectado un accidente, ya hemos contactado a los contactos de familiar y servicio de emergencia.")
        .setStyle(NotificationCompat.BigTextStyle().bigText("Se ha detectado un accidente, ya hemos contactado a los contactos de familiar y servicio de emergencia."))
        .setSmallIcon(R.drawable.icon_app_circle)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()
    notificationManager.notify("Urgente".hashCode(), notification)
    Log.d("sendNotification", "Notification sent using channel: ${MyApp.CHANNEL_ID}")
}

fun sendSmsEme(context: Context, phoneNumber: String, info: Map<String, String?>) {
    val message = "URGENTE!!! \n" +
            "\nHubo un accidente en la siguiente ubicación: https://www.google.com/maps?q=${info["latitude"]},${info["longitude"]}  \n" +
            "\nInformación de la victima:" +
            "\nNombre: ${info["name"]} " +
            "\nEdad: ${info["age"]} años " +
            "\nDirección: ${info["address"]} " +
            "\nTipo de sangre: ${info["bloodType"]} " +
            "\nAltura: ${info["height"]} mts " +
            "\nPeso: ${info["weight"]} kg " +
            "\nGénero: ${info["gender"]} " +
            "\nAlergias: ${info["allergies"]}\n" +
            "\nYa nos encargamos de avisar al familiar correspondiente y compartimos su número de contacto para que pueda contactar con ustedes, en caso de que ustedes quieran contactar al familiar aqui está su número: [Número]."
    try {
        val smsManager = SmsManager.getDefault()
        val parts = smsManager.divideMessage(message)
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
        Toast.makeText(context, "SMS enviado.", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al enviar SMS.", Toast.LENGTH_SHORT).show()
    }
}

fun sendSmsFam(context: Context, phoneNumber: String, info: Map<String, String?>) {
    val message = "URGENTE!!! \n" +
            "\nTu familiar ${info["name"]} ha sufrido un lamentable accidente en la siguiente ubicación: https://www.google.com/maps?q=${info["latitude"]},${info["longitude"]}\n" +
            "\nYa hemos contactado al servicio de emergencia, el número para que te pongas en contacto con ellos y pidas más información es el siguiente: [Número]"
    try {
        val smsManager = SmsManager.getDefault()
        val parts = smsManager.divideMessage(message)
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
        Toast.makeText(context, "SMS enviado.", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al enviar SMS.", Toast.LENGTH_SHORT).show()
    }
}