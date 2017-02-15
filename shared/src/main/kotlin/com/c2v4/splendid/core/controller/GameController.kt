package com.c2v4.splendid.core.controller

import com.c2v4.splendid.core.events.DealCardEvent
import com.c2v4.splendid.core.events.DealNobleEvent
import com.c2v4.splendid.core.events.EventObserver
import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Noble
import com.c2v4.splendid.core.model.Player

class GameController(val players: List<Player>, val board: Board) {
    var eventObservers: MutableList<EventObserver> = mutableListOf()
    var currentPlayer = 0

    fun initialize() {
        eventObservers.add(ModelManipulator(board))
        initializeCards()
        initializeNobles()
    }

    private fun initializeCards() {
        (0..Board.NUMBER_OF_TIERS - 1).forEach {
            tier ->
            (0..Board.NUMBER_OF_CARDS_PER_TIER - 1).forEach {
                dealCard(tier)
            }
        }
    }

    private fun dealCard(tier: Int) {
        val cardDealtToDeal = board.getFirstCard(tier)
        cardDealtToDeal.ifPresent { card -> eventObservers.forEach { it.handle(DealCardEvent(card, tier)) } }
    }

    private fun initializeNobles() {
        val noblesDrawn = mutableListOf<Noble>()
        (0..players.size).forEach {
            board.nobleDeck.poll().ifPresent { noblesDrawn.add(it) }
        }
        if (noblesDrawn.size != players.size + 1) throw IllegalStateException()
        eventObservers.forEach { it.handle(DealNobleEvent(noblesDrawn)) }
    }

    fun registerEventObserver(observer: EventObserver) {
        eventObservers.add(observer)
    }

    fun unregisterEventObserver(observer: EventObserver) {
        eventObservers.remove(observer)
    }
}
