package com.c2v4.splendid.core.model

import com.c2v4.splendid.core.model.Resource.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

internal class CardTest {

    private var card: Card? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun canBuyEqualTest() {
        val costs = mapOf(
                BLACK to 2, RED to 3
        )
        val monies = mapOf(
                BLACK to 2, RED to 3
        )
        card = Card(1, 1, costs, BLACK)

        val canBuy = card!!.canBuy(monies)

        assertTrue(canBuy)
    }

    @Test
    fun canBuyMoreTest() {
        val costs = mapOf(
                BLACK to 2, RED to 3
        )
        val monies = mapOf(
                RED to 6, BLUE to 2, GREEN to 4, BLACK to 3
        )
        card = Card(1, 1, costs, BLACK)

        val canBuy = card!!.canBuy(monies)

        assertTrue(canBuy)
    }


    @Test
    fun canBuyGoldTest() {
        val costs = mapOf(
                BLACK to 2, RED to 3
        )
        val monies = mapOf(
                RED to 2, BLUE to 2, BLACK to 1, GOLD to 2, GREEN to 4
        )
        card = Card(1, 1, costs, RED)

        val canBuy = card!!.canBuy(monies)

        assertTrue(canBuy)
    }

    @Test
    fun canBuyGoldOnlyTest() {
        val costs = mapOf(
                BLACK to 2, RED to 3
        )
        val monies = mapOf(
                BLUE to 2, GOLD to 5, GREEN to 4
        )
        card = Card(1, 1, costs, RED)

        val canBuy = card!!.canBuy(monies)

        assertTrue(canBuy)
    }

    @Test
    fun cannotBuyEmptyWalletTest() {
        val costs = mapOf(
                BLACK to 2, RED to 3
        )
        val monies = mapOf<Resource,Int>( )
        card = Card(1, 1, costs, RED)

        val canBuy = card!!.canBuy(monies)

        assertFalse(canBuy)
    }

    @Test
    fun cannotBuyOnlyOneResourceTest() {
        val costs = mapOf(
                BLACK to 2, RED to 3
        )
        val monies = mapOf(
                RED to 3
        )
        card = Card(1, 1, costs, RED)

        val canBuy = card!!.canBuy(monies)

        assertFalse(canBuy)
    }

    @Test
    fun cannotBuyTest() {
        val costs = mapOf(
                BLACK to 2, RED to 3
        )
        val monies = mapOf(
                GREEN to 2, RED to 4
        )
        card = Card(1, 1, costs, RED)

        val canBuy = card!!.canBuy(monies)

        assertFalse(canBuy)
    }

}