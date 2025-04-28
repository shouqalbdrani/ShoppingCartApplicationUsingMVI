package com.example.shoppingcartapplicationusingmvi.domain.usecase

import com.example.shoppingcartapplicationusingmvi.data.repositories.CartRepository
import com.example.shoppingcartapplicationusingmvi.domain.models.CartItem

class GetCartItemUseCase(private val repository: CartRepository){
    operator fun invoke(): List<CartItem>{
        return repository.getCartItems()

    }
}