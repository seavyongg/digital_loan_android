package com.app.afinal.model
data class Product(
    val id: Int = 0,
    val  name : String,
    val imageUrl : String,
    val price : Double,
    val specs: specs,
) {
    var quantity: Int = 1 // Default quantity is 1
    var isSelected: Boolean = false // Default selection state is false
}

data class specs(
    val os : String,
    val cpu : String,
    val memory : String,
    val screenSize : String,
)