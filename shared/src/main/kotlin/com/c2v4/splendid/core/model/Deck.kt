package com.c2v4.splendid.core.model

import java.util.*

class  Deck  <T> private constructor(cards: List<T>) {
    private val cards: MutableList<T>

    init {
        this.cards = ArrayList(cards)
    }


    fun size(): Int {
        return cards.size
    }

    fun isEmpty(): Boolean {
        return cards.isEmpty()
    }

    fun poll(): Optional<T> {
        if(isEmpty()){
            return Optional.empty()
        }else{
            return Optional.ofNullable(cards.removeAt(0))
        }
    }

    companion object {

        fun <T> shuffledDeck(random: Random, cards: List<T>): Deck<T> {
            val toPut = ArrayList(cards)
            val toDeck = ArrayList<T>(cards.size)
            while (toPut.size > 0) {
                val index = random.nextInt(toPut.size)
                toDeck.add(toPut.removeAt(index))
            }
            return Deck(toDeck)
        }

    }
}
