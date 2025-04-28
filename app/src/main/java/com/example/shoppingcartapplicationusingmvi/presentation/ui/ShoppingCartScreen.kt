package com.example.shoppingcartapplicationusingmvi.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingcartapplicationusingmvi.presentation.vm.ShoppingCartViewModel
import com.example.shoppingcartapplicationusingmvi.presentation.vm.ShoppingCartViewModelFactory
import com.example.shoppingcartapplicationusingmvi.presentation.ShoppingCartViewState
import com.example.shoppingcartapplicationusingmvi.domain.models.ShoppingCartIntent
import com.example.shoppingcartapplicationusingmvi.domain.models.CartItem
import com.example.shoppingcartapplicationusingmvi.data.repositories.CartRepositoryImpl
import com.example.shoppingcartapplicationusingmvi.domain.usecase.GetCartItemUseCase
import com.example.shoppingcartapplicationusingmvi.domain.usecase.AddItemToCartUseCase
import com.example.shoppingcartapplicationusingmvi.domain.usecase.RemoveItemFromCartUseCase

@Composable
fun ShoppingCartScreen(
    modifier: Modifier = Modifier
) {
    val cartRepository = remember { CartRepositoryImpl() }
    val getCartItemUseCase = remember { GetCartItemUseCase(cartRepository) }
    val addItemToCartUseCase = remember { AddItemToCartUseCase(cartRepository) }
    val removeItemFromCartUseCase = remember { RemoveItemFromCartUseCase(cartRepository) }

    val viewModel: ShoppingCartViewModel = viewModel(
        factory = ShoppingCartViewModelFactory(getCartItemUseCase, addItemToCartUseCase , removeItemFromCartUseCase)
    )

    val viewState by viewModel.viewStateFlow.collectAsState()

    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(ShoppingCartIntent.LoadCartItems)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        when (viewState) {
            is ShoppingCartViewState.Loading -> {
                Text(
                    text = "Loading ......",
                    modifier = Modifier
                        .padding(16.dp))
            }
            is ShoppingCartViewState.Success -> {
                val items = (viewState as ShoppingCartViewState.Success).cartItems

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "${item.itemName} - ${item.itemPrice}SR")
                                    Text(
                                        text = "Quantity: ${item.quantity}")
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.handleIntent(
                                            ShoppingCartIntent.RemoveItem(item.itemId)
                                        )
                                    }
                                ) {
                                    Icon (
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Item"
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = itemPrice,
                    onValueChange = { itemPrice = it },
                    label = { Text("Item Price") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = itemQuantity,
                    onValueChange = { itemQuantity = it },
                    label = { Text("Item Quantity") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (itemName.isNotBlank() && itemPrice.isNotBlank() && itemQuantity.isNotBlank()) {
                            viewModel.handleIntent(
                                ShoppingCartIntent.AddItem(
                                    CartItem(
                                        itemId = (0..1000).random(), // create ID Random
                                        itemName = itemName,
                                        itemPrice = itemPrice.toDoubleOrNull() ?: 0.0,
                                        quantity = itemQuantity.toIntOrNull() ?: 1
                                    )
                                )
                            )

                            itemName = ""
                            itemPrice = ""
                            itemQuantity = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Item")
                }
            }
            is ShoppingCartViewState.Error -> {
                val message = (viewState as ShoppingCartViewState.Error).message
                Text(text = "Error: $message", modifier = modifier.padding(16.dp))
            }
        }
    }
}
