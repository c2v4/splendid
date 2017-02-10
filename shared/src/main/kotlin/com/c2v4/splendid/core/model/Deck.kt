package com.c2v4.splendid.core.model

import java.util.*

class Deck private constructor(cards: List<Card>) {
    private val cards: List<Card>

    init {
        this.cards = ArrayList(cards)

    }

    fun peek(): Card {
        return cards[0]
    }

    fun size(): Int {
        return cards.size
    }

    companion object {

        fun shuffledDeck(random: Random, cards: List<Card>): Deck {
            val toPut = ArrayList(cards)
            val toDeck = ArrayList<Card>(cards.size)
            while (toPut.size > 0) {
                val index = random.nextInt(toPut.size)
                toDeck.add(toPut.removeAt(index))
            }
            return Deck(toDeck)
        }

        fun withoutFirst(deck: Deck): Deck {
            val toNewDeck = ArrayList(deck.cards)
            toNewDeck.removeAt(0)
            return Deck(toNewDeck)
        }
    }
}
