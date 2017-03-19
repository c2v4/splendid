package com.c2v4.splendid.component

import com.c2v4.splendid.component.cardtable.CardTableModel
import com.c2v4.splendid.component.playerstable.PlayerTableModel
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateModel
import com.c2v4.splendid.component.reservedcard.ReservedCardsModel
import com.c2v4.splendid.component.resourcetable.ResourceTableModel

class CommonModel(var playerEvent: PlayerEvent,
                  val cardTableModel: CardTableModel,
                  val resourceModel: ResourceTableModel,
                  val playerTableModel: PlayerTableModel,
                  val reservedCardsModel: ReservedCardsModel) {

    private val turnListeners = mutableListOf<(playerTurn: Boolean) -> Unit>()
    private val actionCorrectListeners = mutableListOf<(playerTurn: Boolean) -> Unit>()

    private var playerTurn = false
    private var actionCorrect = false

    companion object {
        fun empty(cardTableModel: CardTableModel = CardTableModel.empty(),
                  resourceModel: ResourceTableModel = ResourceTableModel.empty(),
                  playerTableModel: PlayerTableModel = PlayerTableModel("",PlayerStateModel.empty(),
                          mutableListOf(PlayerStateModel.empty())),
                  reservedCardsModel: ReservedCardsModel = ReservedCardsModel.empty()): CommonModel {
            return CommonModel(PlayerEvent.NONE,
                    cardTableModel,
                    resourceModel,
                    playerTableModel,
                    reservedCardsModel)
        }
    }

    fun setPlayerTurn(playerTurn: Boolean) {
        println("playerTurn = ${playerTurn}")
        this.playerTurn = playerTurn
        turnListeners.forEach { it.invoke(playerTurn) }
    }

    fun addTurnListener(listener: (isPlayerTurn: Boolean) -> Unit) {
        turnListeners.add(listener)
    }

    fun setActionCorrect(actionCorrect: Boolean) {
        this.actionCorrect = actionCorrect
        actionCorrectListeners.forEach { it.invoke(actionCorrect && playerTurn) }
    }

    fun addActionCorrectListener(listener: (actionCorrect: Boolean) -> Unit) {
        actionCorrectListeners.add(listener)
    }

    fun getPlayerTurn(): Boolean {
        return playerTurn
    }

    fun getActionCorrect(): Boolean {
        return actionCorrect
    }

}