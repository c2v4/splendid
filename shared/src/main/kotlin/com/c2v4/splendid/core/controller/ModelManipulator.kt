package com.c2v4.splendid.core.controller

import com.c2v4.splendid.core.events.DealCardEvent
import com.c2v4.splendid.core.events.EventObserver
import com.c2v4.splendid.core.model.Board

class ModelManipulator(val board: Board) : EventObserver {

    override fun handle(event: DealCardEvent) {
        board.dealCard(event.tier,event.card)
    }

}