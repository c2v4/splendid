package com.c2v4.splendid.core.model

import java.util.*


class Board(val cardDecks: List<Deck<Card>>,
            val nobleDeck: Deck<Noble>,
            numberOfNobles: Int = NUMBER_OF_NOBLES_NOT_DEFINED,
            var availableCoins: Map<Resource, Int> = mapOf(),
            val cardsOnBoard: Array<Array<Card?>> = arrayOf(arrayOfNulls(NUMBER_OF_CARDS_PER_TIER),
                    arrayOfNulls(NUMBER_OF_CARDS_PER_TIER),
                    arrayOfNulls(NUMBER_OF_CARDS_PER_TIER)),
            val noblesOnBoard: Array<Noble?> = arrayOfNulls(numberOfNobles)
) {

    companion object {
        val NUMBER_OF_TIERS = 3
        val NUMBER_OF_CARDS_PER_TIER = 4
        val NUMBER_OF_NOBLES_NOT_DEFINED = -1
        val MINIMUM_NOBLES = 3
    }

    fun dealCard(tier: Int, position: Int): Optional<Card> {
        val tierOnBoard = cardsOnBoard[tier]
        if (tierOnBoard[position] != null) {
            throw IllegalStateException()
        }
        val card = cardDecks[tier].poll()
        card.ifPresent { tierOnBoard[position] = it }
        return card
    }

    fun dealNoble(position: Int): Optional<Noble> {
        if (noblesOnBoard[position] != null) {
            throw IllegalStateException()
        }
        val noble = nobleDeck.poll()
        noble.ifPresent { noblesOnBoard[position] = it }
        return noble
    }
//
//    fun addNoble(noble: Noble?, position: Int) {
//        this.noblesOnBoard[position] = noble
//    }
}
