package com.app.afinal.data

import android.util.Log
import com.app.afinal.model.Product

object CartData {
    val _cartItems : MutableList<Product> = mutableListOf()
    val cartItems: List<Product> get() = _cartItems.toList() //_cartItems is private to prevent external modification

    //add a product to the cart
    fun addToCart(product: Product){
        product.isSelected = false
        val existingProduct = cartItems.find { it.id == product.id }
        if(existingProduct != null) {
            // If product already exists in cart, increase quantity
            existingProduct.quantity += 1
        } else {
            // If product does not exist, add it to the cart
            _cartItems.add(product)
            product.quantity = 1
        }
    }
    //add all
    fun addAll(product: List<Product>){
        product.forEach { it.isSelected = false } // Reset selection state
        _cartItems.addAll(product)
        Log.d("Cart", "Products added to cart, Total items in cart: ${_cartItems.size}")
    }
    //clear a product from the cart
    fun clear(){
        _cartItems.clear()
    }


}