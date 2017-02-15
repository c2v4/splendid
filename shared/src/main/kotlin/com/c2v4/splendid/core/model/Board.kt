package com.c2v4.splendid.core.model

import java.util.*


class Board(val cardDecks: List<Deck<Card>>, val nobleDeck:Deck<Noble>) {
    val cardsOnBoard: List<MutableList<Card>> = listOf(mutableListOf(), mutableListOf(), mutableListOf())
    val nobles:MutableList<Noble> = mutableListOf()

    companion object {
        val NUMBER_OF_TIERS = 3
        val NUMBER_OF_CARDS_PER_TIER = 4
    }

    fun dealCard(tier: Int, card: Card) {
        val tierOnBoard = cardsOnBoard[tier]
        if (tierOnBoard.size > NUMBER_OF_CARDS_PER_TIER - 1) {
            throw IllegalStateException()
        }
        cardDecks[tier].poll()
        tierOnBoard.add(card)
    }

    fun getFirstCard(tier: Int): Optional<Card> {
        return cardDecks[tier].peek()
    }

    fun addNobles(nobles:List<Noble>){
        this.nobles.addAll(nobles)
    }
}
