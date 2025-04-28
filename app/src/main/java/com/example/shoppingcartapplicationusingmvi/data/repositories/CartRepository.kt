package com.example.shoppingcartapplicationusingmvi.data.repositories

import com.example.shoppingcartapplicationusingmvi.domain.models.CartItem

interface CartRepository {
   fun getCartItems(): List <CartItem> // return list of items
   fun addItem(item: CartItem) // add item
   fun removeItem(itemId : Int) // remove item

}