package com.c2v4.splendid.core.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.Test
import java.util.*

class BoardTest {
    @Test
    fun initializeTest() {
        Board(listOf(Deck.shuffledDeck(Random(), listOf()),
                Deck.shuffledDeck(Random(), listOf()),
                Deck.shuffledDeck(Random(), listOf())),
                Deck.shuffledDeck(Random(), listOf()),
                3)
    }

    @Test
    fun shouldFailWhenNoNoblesAndNumberNotSpecified(){
        assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
            Board(listOf(Deck.shuffledDeck(Random(), listOf()),
                    Deck.shuffledDeck(Random(), listOf()),
                    Deck.shuffledDeck(Random(), listOf())),
                    Deck.shuffledDeck(Random(), listOf())
                    )
        }
    }
    @Test
    fun shouldInitializeWithGivenValues(){
        val card = Card(1, 1, mapOf(), Resource.RED)
        val board = Board(listOf(Deck.shuffledDeck(Random(), listOf()),
                Deck.shuffledDeck(Random(), listOf()),
                Deck.shuffledDeck(Random(), listOf())),
                Deck.shuffledDeck(Random(), listOf()),
                cardsOnBoard = arrayOf(arrayOf<Card?>(card)),
                noblesOnBoard = arrayOf(Noble(mapOf()))
        )
        assertThat(board.cardsOnBoard[0]).contains(card)
        assertThat(board.noblesOnBoard[0]).isNotNull()
    }
}