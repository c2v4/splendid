package com.c2v4.splendid.component.reservedcard

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.c2v4.splendid.component.CommonModel
import com.c2v4.splendid.component.PlayerEvent
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateModel
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.util.canBuy
import com.c2v4.splendid.core.util.merge
import com.c2v4.splendid.network.message.game.ReservedCardBuy

class ReservedCardsController(val reservedCardsView: ReservedCardsView,
                              val reservedCardsModel: ReservedCardsModel,
                              val playerStateModel: PlayerStateModel,
                              val commonModel: CommonModel) {

    private var cardClicked: Card? = null
    private var selectedPosition = -1
    private var lastActor: Actor? = null

    init {
        reservedCardsView.onClickListeners.add { position, cardActor ->
            val card = reservedCardsModel.cards[position]
            if (commonModel.playerEvent == PlayerEvent.NONE) {
                if (card != null) {
                    if (cardClicked == null) {
                        val playerWealth = playerStateModel.wallet.merge(playerStateModel.cardResources)
                        if (canBuy(card.cost, playerWealth)) {
                            selectedPosition = position
                            cardClicked = card
                            cardActor.color = Color.RED
                            commonModel.playerEvent = PlayerEvent.BUY_RESERVED_CARD
                            lastActor = cardActor
                            commonModel.setActionCorrect(true)
                        }
                    }
                }
            } else {
                if (commonModel.playerEvent == PlayerEvent.BUY_RESERVED_CARD && card == cardClicked) {
                    resetCard(cardActor)
                }
            }
        }
    }

    private fun resetCard(cardActor: Actor?) {
        selectedPosition = -1
        cardClicked = null
        cardActor?.color = Color.WHITE
        commonModel.playerEvent = PlayerEvent.NONE
        lastActor = null
        commonModel.setActionCorrect(false)
    }

    fun getEvent(): ReservedCardBuy {
        val event = ReservedCardBuy(selectedPosition)
        resetCard(lastActor)
        return event
    }
}