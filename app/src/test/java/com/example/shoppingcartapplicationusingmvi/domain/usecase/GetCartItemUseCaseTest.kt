package com.example.shoppingcartapplicationusingmvi.domain.usecase

import com.example.shoppingcartapplicationusingmvi.data.repositories.CartRepository
import com.example.shoppingcartapplicationusingmvi.domain.models.CartItem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetCartItemUseCaseTest {

    class FakeCartRepository : CartRepository {
        private val items = mutableListOf<CartItem>()

        override fun getCartItems(): List<CartItem> = items.toList()

        override fun addItem(item: CartItem) {
            items.add(item)
        }

        override fun removeItem(itemId: Int) {
            items.removeAll { it.itemId == itemId }
        }

        fun seedItems(vararg cartItems: CartItem) {
            items.addAll(cartItems)
        }

        fun clear() = items.clear()
    }

    private lateinit var fakeRepository: FakeCartRepository
    private lateinit var useCase: GetCartItemUseCase

    @Before
    fun setUp() {
        fakeRepository = FakeCartRepository()
        useCase = GetCartItemUseCase(fakeRepository)
    }

    @After
    fun tearDown() {
        fakeRepository.clear()
    }

    @Test
    fun `invoke returns all cart items`() {
        val item1 = CartItem(1, "Shoes", 200.0, 1)
        val item2 = CartItem(2, "Bag", 300.0, 2)
        fakeRepository.seedItems(item1, item2)

        val result = useCase()

        assertEquals(2, result.size)
        assertTrue(result.contains(item1))
        assertTrue(result.contains(item2))
    }

    @Test
    fun `invoke returns empty list when no items exist`() {
        val result = useCase()
        assertTrue(result.isEmpty())
    }
}
