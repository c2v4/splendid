package com.c2v4.splendid.core.util

import com.c2v4.splendid.core.model.*
import java.util.*

private val random = Random()

fun randomCard(): Card {
    val cost = randomCost()
    val resource = randomResource()
    return Card(random.nextInt(3), random.nextInt(6), cost, resource)
}

private fun randomCost(): Map<Resource, Int> {
    val cost = mutableMapOf<Resource, Int>()
    (0..(random.nextInt(3) + 1)).forEach { cost.put(randomResource(), random.nextInt(5) + 1) }
    return cost
}

fun randomResource(): Resource {
    val resourceValues = Resource.values()
    return resourceValues[random.nextInt(resourceValues.size)]
}

fun randomCardDeck():Deck<Card> {
    val cards = mutableListOf<Card>()
    (0..(random.nextInt(4)+6)).forEach { cards.add(randomCard()) }
    return Deck.shuffledDeck(random,cards)
}

fun randomNobleDeck():Deck<Noble>{
    val nobles = mutableListOf<Noble>()
    (0..(random.nextInt(4)+6)).forEach { nobles.add(Noble(randomCost())) }
    return Deck.shuffledDeck(random,nobles)
}

fun randomBoard(numberOfNobles:Int):Board{
    return Board(listOf(randomCardDeck(),randomCardDeck(),randomCardDeck()), randomNobleDeck(),numberOfNobles)
}