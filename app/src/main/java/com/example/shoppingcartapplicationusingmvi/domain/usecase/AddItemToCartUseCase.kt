package com.example.shoppingcartapplicationusingmvi.domain.usecase

import com.example.shoppingcartapplicationusingmvi.data.repositories.CartRepository
import com.example.shoppingcartapplicationusingmvi.domain.models.CartItem

class AddItemToCartUseCase(private val repository: CartRepository){
    operator fun invoke (item :CartItem){
        repository.addItem(item)
    }
}