package com.c2v4.splendid.core.model

import java.util.*


class Board(val decks: List<Deck>) {
    val cardsOnBoard: List<MutableList<Card>> = listOf(mutableListOf(), mutableListOf(), mutableListOf())

    companion object {
        val NUMBER_OF_TIERS = 3
        val NUMBER_OF_CARDS_PER_TIER = 4
    }

    fun dealCard(tier: Int, card: Card) {
        val tierOnBoard = cardsOnBoard[tier]
        if (tierOnBoard.size > NUMBER_OF_CARDS_PER_TIER - 1) {
            throw IllegalStateException()
        }
        decks[tier].poll()
        tierOnBoard.add(card)
    }

    fun getFirstCard(tier: Int): Optional<Card> {
        return decks[tier].peek()
    }
}
