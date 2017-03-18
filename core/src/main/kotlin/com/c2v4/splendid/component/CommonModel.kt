package com.c2v4.splendid.component

import com.c2v4.splendid.component.cardtable.CardTableModel
import com.c2v4.splendid.component.playerstable.PlayerTableModel
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateModel
import com.c2v4.splendid.component.reservedcard.ReservedCardsModel
import com.c2v4.splendid.component.resourcetable.ResourceTableModel
import com.c2v4.splendid.controller.PlayerEvent

class CommonModel(var playerEvent: PlayerEvent,
                  val cardTableModel: CardTableModel,
                  val resourceModel: ResourceTableModel,
                  val playerTableModel: PlayerTableModel,
                  val reservedCardsModel: ReservedCardsModel) {

    private val turnListeners = mutableListOf<(playerTurn:Boolean)->Unit>()

    private var playerTurn = false

    companion object {
        fun empty(cardTableModel: CardTableModel = CardTableModel.empty(),
                  resourceModel: ResourceTableModel = ResourceTableModel.empty(),
                  playerTableModel: PlayerTableModel = PlayerTableModel(PlayerStateModel.empty(),
                          mutableListOf(PlayerStateModel.empty())),
                  reservedCardsModel: ReservedCardsModel = ReservedCardsModel.empty()): CommonModel {
            return CommonModel(PlayerEvent.NONE,
                    cardTableModel,
                    resourceModel,
                    playerTableModel,
                    reservedCardsModel)
        }
    }
    fun setPlayerTurn(playerTurn:Boolean){
        this. playerTurn =playerTurn
        turnListeners.forEach { it.invoke(playerTurn) }
    }

    fun addTurnListener(listener: (isPlayerTurn:Boolean) -> Unit) {
        turnListeners.add(listener)
    }
}