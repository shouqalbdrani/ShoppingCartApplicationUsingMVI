package com.example.shoppingcartapplicationusingmvi.domain.usecase

import com.example.shoppingcartapplicationusingmvi.data.repositories.CartRepository

class RemoveItemFromCartUseCase(
    private val cartRepository: CartRepository
) {
      operator fun invoke(itemId: Int) {
        cartRepository.removeItem(itemId)
    }
}
