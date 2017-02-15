package com.c2v4.splendid.core.controller

import com.c2v4.splendid.core.events.DealCardEvent
import com.c2v4.splendid.core.events.DealNobleEvent
import com.c2v4.splendid.core.events.EventObserver
import com.c2v4.splendid.core.model.*
import com.c2v4.splendid.core.model.Resource.*
import com.nhaarman.mockito_kotlin.any
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.*

class GameControllerTest {

    @Before
    fun setUp() {

    }

    @Test
    fun initializeTest() {
        val cards = listOf(
                listOf(
                        Card(1, 2, mapOf(), RED),
                        Card(1, 3, mapOf(), RED),
                        Card(1, 4, mapOf(), RED),
                        Card(1, 5, mapOf(), RED)),
                listOf(
                        Card(2, 2, mapOf(), RED),
                        Card(2, 3, mapOf(), RED),
                        Card(2, 4, mapOf(), RED),
                        Card(2, 5, mapOf(), RED)),
                listOf(
                        Card(3, 2, mapOf(), RED),
                        Card(3, 3, mapOf(), RED),
                        Card(3, 4, mapOf(), RED),
                        Card(3, 5, mapOf(), RED)))
        val nobles = listOf(
                Noble(mapOf(BLACK to 1)),
                Noble(mapOf(BLUE to 1)),
                Noble(mapOf(BLUE to 2)),
                Noble(mapOf(RED to 2)),
                Noble(mapOf(RED to 1))
                )
        val board = Board(listOf(
                Deck.shuffledDeck(Random(), cards[0]),
                Deck.shuffledDeck(Random(), cards[1]),
                Deck.shuffledDeck(Random(), cards[2])), Deck.shuffledDeck(Random(),nobles))
        val gameController = GameController(listOf(Player(),Player(),Player(),Player()), board)
        val eventObserver = mock(EventObserver::class.java)
        gameController.registerEventObserver(eventObserver)

        gameController.initialize()

        cards.forEachIndexed { tier, list ->
            list.forEach {   verify(eventObserver, Mockito.times(1)).handle(DealCardEvent(it,tier)) }
        }

         verify(eventObserver, com.nhaarman.mockito_kotlin.times(1)).handle(any<DealNobleEvent>())

    }

}