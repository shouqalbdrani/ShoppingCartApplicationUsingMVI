package com.example.shoppingcartapplicationusingmvi.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapplicationusingmvi.domain.models.CartItem
import com.example.shoppingcartapplicationusingmvi.domain.models.ShoppingCartIntent
import com.example.shoppingcartapplicationusingmvi.domain.usecase.AddItemToCartUseCase
import com.example.shoppingcartapplicationusingmvi.domain.usecase.GetCartItemUseCase
import com.example.shoppingcartapplicationusingmvi.domain.usecase.RemoveItemFromCartUseCase
import com.example.shoppingcartapplicationusingmvi.presentation.ShoppingCartViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShoppingCartViewModel(
    private val getCartItemUseCase: GetCartItemUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase
) : ViewModel() {

    private val _viewStateFlow: MutableStateFlow<ShoppingCartViewState> =
        MutableStateFlow(ShoppingCartViewState.Loading)
    val viewStateFlow: StateFlow<ShoppingCartViewState> = _viewStateFlow.asStateFlow()

    private val cartItems = mutableListOf<CartItem>()

    fun handleIntent(intent: ShoppingCartIntent) {
        viewModelScope.launch {
            val newState = reduce(intent)
            _viewStateFlow.update { newState }
        }
    }

    private fun reduce(intent: ShoppingCartIntent): ShoppingCartViewState {
        return when (intent) {
            is ShoppingCartIntent.LoadCartItems -> {
                try {
                    val items = getCartItemUseCase()
                    cartItems.clear()
                    cartItems.addAll(items)
                    ShoppingCartViewState.Success(cartItems.toList())
                } catch (e: Exception) {
                    ShoppingCartViewState.Error(e.message ?: "Failed to load cart")
                }
            }

            is ShoppingCartIntent.AddItem -> {
                addItemToCartUseCase(intent.item)
                cartItems.add(intent.item)
                ShoppingCartViewState.Success(cartItems.toList())
            }

            is ShoppingCartIntent.RemoveItem -> {
                removeItemFromCartUseCase(intent.itemId)
                cartItems.removeAll { it.itemId == intent.itemId }
                ShoppingCartViewState.Success(cartItems.toList())
            }

            is ShoppingCartIntent.UpdateQuantity -> TODO()
        }
    }
}



class ShoppingCartViewModelFactory(
    private val getCartItemUseCase: GetCartItemUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingCartViewModel::class.java)) {
            return ShoppingCartViewModel(getCartItemUseCase, addItemToCartUseCase, removeItemFromCartUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

