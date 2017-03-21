package com.c2v4.splendid.component.cardtable

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.c2v4.splendid.component.CommonModel
import com.c2v4.splendid.component.PlayerEvent
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateModel
import com.c2v4.splendid.component.reservedcard.ReservedCardsModel
import com.c2v4.splendid.component.resourcetable.ResourceTableModel
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.core.util.canBuy
import com.c2v4.splendid.core.util.merge
import com.c2v4.splendid.entity.CardActor
import com.c2v4.splendid.network.message.game.ReserveCard

class CardTableController(val view: CardTableView,
                          val model: CardTableModel,
                          val reservedCardsModel: ReservedCardsModel,
                          val resourceTableModel: ResourceTableModel,
                          val playerStateModel: PlayerStateModel,
                          val commonModel: CommonModel) {

    var cardClicked: Card? = null
    var lastActor: Actor? = null
    var tier: Int = -1
    var position: Int = -1

    init {
        view.registerController(this)
    }

    fun click(position: Int, tier: Int, actor: CardActor) {
        if (setOf(PlayerEvent.NONE,
                PlayerEvent.RESERVE,
                PlayerEvent.BUY).contains(commonModel.playerEvent)) {
            val card = model.cards[tier][position]
            if (card != null) {
                this.tier = tier
                this.position = position
                if (cardClicked == null) {
                    if (canBuy(card.costs, playerStateModel.wallet.merge(playerStateModel.cards))) {
                        cardClicked = card
                        actor.color = Color.RED
                        commonModel.playerEvent = PlayerEvent.BUY
                        lastActor = actor
                        commonModel.setActionCorrect(true)
                    } else {
                        if (playerStateModel.cardsReserved < 3) {
                            cardClicked = card
                            reserve(actor)
                        }
                    }
                } else {
                    if (card == cardClicked) {
                        when (commonModel.playerEvent) {
                            PlayerEvent.BUY -> {
                                if (playerStateModel.cardsReserved < 3) {
                                    reserve(actor)
                                } else {
                                    actor.color = Color.WHITE
                                    commonModel.playerEvent = PlayerEvent.NONE
                                    commonModel.setActionCorrect(false)
                                    lastActor = null
                                    cardClicked = null
                                }
                            }
                            PlayerEvent.RESERVE -> {
                                actor.color = Color.WHITE
                                commonModel.playerEvent = PlayerEvent.NONE
                                commonModel.setActionCorrect(false)
                                lastActor = null
                                cardClicked = null
                            }
                            PlayerEvent.NONE -> throw IllegalStateException()
                            PlayerEvent.GET_COINS -> throw IllegalStateException()
                        }
                    }
                }
            }
        }
    }

    private fun reserve(actor: CardActor) {
        actor.color = Color.YELLOW
        commonModel.playerEvent = PlayerEvent.RESERVE
        lastActor = actor
        val amountToReturn = playerStateModel.wallet.values.sum() - 10 + if (resourceTableModel.resourcesAvailable.getOrElse(
                Resource.GOLD,
                { 0 }) > 0) 1 else 0
        commonModel.setActionCorrect(amountToReturn < 1)
    }

    fun getReserveEvent(returned:Map<Resource,Int> = mapOf()): Any? {
        cardClicked = null
        lastActor!!.color = Color.WHITE
        commonModel.playerEvent = PlayerEvent.NONE
        commonModel.setActionCorrect(false)
        return ReserveCard(tier, position,returned)
    }


}

enum class CardClickState {
    NONE, RESERVE, BUY
}
