package com.example.shoppingcartapplicationusingmvi.domain.models

data class CartItem(
    val itemId : Int,
    val itemName : String ,
    val itemPrice : Double ,
    val quantity : Int
)