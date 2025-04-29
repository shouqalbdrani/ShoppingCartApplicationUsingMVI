package com.example.shoppingcartapplicationusingmvi.domain.usecase

import com.example.shoppingcartapplicationusingmvi.data.repositories.CartRepository
import com.example.shoppingcartapplicationusingmvi.domain.models.CartItem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test

class AddItemToCartUseCaseTest {


    class FakeCartRepository : CartRepository {
        val addedItems = mutableListOf<CartItem>()

        override fun addItem(item: CartItem) {
            addedItems.add(item)
        }

        override fun removeItem(itemId: Int) {
            addedItems.removeAll { it.itemId == itemId }
        }

        override fun getCartItems(): List<CartItem> {
            return addedItems.toList()
        }
    }

    private lateinit var fakeRepository: FakeCartRepository
    private lateinit var useCase: AddItemToCartUseCase

    @Before
    fun setUp() {
        fakeRepository = FakeCartRepository()
        useCase = AddItemToCartUseCase(fakeRepository)
    }

    @After
    fun tearDown() {
        fakeRepository.addedItems.clear()
    }

    @Test
    fun `invoke should add item to repository`() {
        val item = CartItem(itemId = 1, itemName = "Test Item", itemPrice = 100.0, quantity = 2)

        useCase(item)

        assertEquals(1, fakeRepository.addedItems.size)
        assertTrue(fakeRepository.addedItems.contains(item))
    }
}
