package com.example.shoppingcartapplicationusingmvi.presentation

import com.example.shoppingcartapplicationusingmvi.domain.models.CartItem

sealed class ShoppingCartViewState {

    object Loading : ShoppingCartViewState()
    data class Error(val message : String ) : ShoppingCartViewState()
    data class Success (val cartItems: List<CartItem>) : ShoppingCartViewState()

}