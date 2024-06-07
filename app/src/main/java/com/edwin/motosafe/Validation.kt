package com.edwin.motosafe

// Agrega la función de validación aquí
fun isValidEmail(email: String): Boolean {
    val emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
    return email.matches(emailRegex)
}

