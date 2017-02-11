package com.c2v4.splendid.core.model

import java.util.*

class Deck private constructor(cards: List<Card>) {
    private val cards: MutableList<Card>

    init {
        this.cards = ArrayList(cards)
    }

    fun peek(): Optional<Card> {
        if (isEmpty()){
            return Optional.empty()
        }else{
            return Optional.of(cards[0])
        }
    }

    fun size(): Int {
        return cards.size
    }

    fun isEmpty(): Boolean {
        return cards.isEmpty()
    }

    fun poll(): Optional<Card> {
        if(isEmpty()){
            return Optional.empty()
        }else{
            return Optional.ofNullable(cards.removeAt(0))
        }
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
