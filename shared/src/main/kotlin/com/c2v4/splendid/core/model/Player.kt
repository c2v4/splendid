package com.c2v4.splendid.core.model

class Player(cards: List<Card> = listOf(),var wallet:Map<Resource,Int> = mutableMapOf()) {
    val cards: MutableList<Card> = cards.toMutableList()
}