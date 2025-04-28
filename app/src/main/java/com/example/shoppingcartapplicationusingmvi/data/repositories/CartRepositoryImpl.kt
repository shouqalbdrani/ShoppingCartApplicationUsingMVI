package com.example.shoppingcartapplicationusingmvi.data.repositories

import com.example.shoppingcartapplicationusingmvi.domain.models.CartItem

class CartRepositoryImpl : CartRepository {

    private val cartItems = mutableListOf<CartItem>() // store items

    override fun getCartItems(): List<CartItem> {
       return cartItems
    }

    override fun addItem(item: CartItem) {
        cartItems.add(item)
    }

    override fun removeItem(itemId: Int) {
        cartItems.removeAll{it.itemId == itemId}
    }
}