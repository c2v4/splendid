package com.c2v4.splendid.core.controller

import com.c2v4.splendid.core.events.DealCardEvent
import com.c2v4.splendid.core.events.EventObserver
import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Deck
import com.c2v4.splendid.core.model.Resource
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
                        Card(1, 2, mapOf(), Resource.RED),
                        Card(1, 3, mapOf(), Resource.RED),
                        Card(1, 4, mapOf(), Resource.RED),
                        Card(1, 5, mapOf(), Resource.RED)),
                listOf(
                        Card(2, 2, mapOf(), Resource.RED),
                        Card(2, 3, mapOf(), Resource.RED),
                        Card(2, 4, mapOf(), Resource.RED),
                        Card(2, 5, mapOf(), Resource.RED)),
                listOf(
                        Card(3, 2, mapOf(), Resource.RED),
                        Card(3, 3, mapOf(), Resource.RED),
                        Card(3, 4, mapOf(), Resource.RED),
                        Card(3, 5, mapOf(), Resource.RED)))

        val board = Board(listOf(
                Deck.shuffledDeck(Random(), cards[0]),
                Deck.shuffledDeck(Random(), cards[1]),
                Deck.shuffledDeck(Random(), cards[2])))
        val gameController = GameController(listOf(), board)
        val eventObserver = mock(EventObserver::class.java)
        gameController.registerEventObserver(eventObserver)

        gameController.initialize()

        cards.forEachIndexed { tier, list ->
            list.forEach {  it -> verify(eventObserver, Mockito.times(1)).handle(DealCardEvent(it,tier)) }
        }

    }

}