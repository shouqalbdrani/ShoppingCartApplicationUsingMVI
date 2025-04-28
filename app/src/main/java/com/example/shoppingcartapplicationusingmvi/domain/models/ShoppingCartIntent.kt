package com.example.shoppingcartapplicationusingmvi.domain.models

sealed class ShoppingCartIntent{
    object LoadCartItems : ShoppingCartIntent()
    data class AddItem(val item : CartItem) : ShoppingCartIntent()
    data class RemoveItem(val itemId : Int) : ShoppingCartIntent()
    data class UpdateQuantity(val itemId : Int , val newQuantity : Int) : ShoppingCartIntent()
}