package com.edwin.motosafe

data class User(
    var name: String = "",
    var age: Int = 0,
    var address: String = "",
    var gender: String = "",
    var height: Double = 0.0,
    var weight: Double = 0.0,
    var bloodType: String = "",
    var allergies: String = "",
    var email: String = ""
)

data class Acceleration(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0
) {
    // Constructor sin argumentos
    constructor() : this(0.0, 0.0, 0.0)
}

data class Location(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) {
    // Constructor sin argumentos necesario para Firebase
    constructor() : this(0.0, 0.0)
}

data class Accident(
    val id: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val accelerationX: Double = 0.0,
    val accelerationY: Double = 0.0,
    val accelerationZ: Double = 0.0
)