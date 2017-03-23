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
import com.c2v4.splendid.network.message.game.CardBuy
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
                if (cardClicked == null) {
                    val playerWealth = playerStateModel.wallet.merge(playerStateModel.cardResources)
                    if (canBuy(card.cost, playerWealth)) {
                        this.tier = tier
                        this.position = position
                        cardClicked = card
                        actor.color = Color.RED
                        commonModel.playerEvent = PlayerEvent.BUY
                        lastActor = actor
                        commonModel.setActionCorrect(canBuyWithoutInteraction(card, playerWealth))
                    } else {
                        if (playerStateModel.cardsReserved < 3) {
                            cardClicked = card
                            reserve(actor, tier, position)
                        }
                    }
                } else {
                    if (card == cardClicked) {
                        when (commonModel.playerEvent) {
                            PlayerEvent.BUY -> {
                                if (playerStateModel.cardsReserved < 3) {
                                    reserve(actor, tier, position)
                                } else {
                                    resetCard(actor)
                                }
                            }
                            PlayerEvent.RESERVE -> {
                                resetCard(actor)
                            }
                            PlayerEvent.NONE -> throw IllegalStateException()
                            PlayerEvent.GET_COINS -> throw IllegalStateException()
                        }
                    }
                }
            }
        }
    }

    private fun canBuyWithoutInteraction(card: Card, playerWealth: Map<Resource, Int>): Boolean {
//        if (canBuy(card.cost, playerWealth.filter { it.key != Resource.GOLD })) {
//            return true
//        } else {
//            val typesOfResourcesPaidWidthGold = playerWealth.filter { it.key != Resource.GOLD }.subtract(
//                    card.cost).count { it.value < 0 }
//            return typesOfResourcesPaidWidthGold < 2
//        }
        return true
    }

    private fun resetCard(actor: CardActor) {
        tier = -1
        position = -1
        actor.color = Color.WHITE
        commonModel.playerEvent = PlayerEvent.NONE
        commonModel.setActionCorrect(false)
        lastActor = null
        cardClicked = null
    }

    private fun reserve(actor: CardActor, tier: Int, position: Int) {
        this.tier = tier
        this.position = position
        actor.color = Color.YELLOW
        commonModel.playerEvent = PlayerEvent.RESERVE
        lastActor = actor
        val amountToReturn = playerStateModel.wallet.values.sum() - 10 + if (resourceTableModel.resourcesAvailable.getOrElse(
                Resource.GOLD,
                { 0 }) > 0) 1 else 0
        commonModel.setActionCorrect(amountToReturn < 1)
    }

    fun getReserveEvent(returned: Map<Resource, Int> = mapOf()): Any? {
        cardClicked = null
        lastActor!!.color = Color.WHITE
        commonModel.playerEvent = PlayerEvent.NONE
        commonModel.setActionCorrect(false)
        return ReserveCard(tier, position, returned)
    }

    fun getBuyEvent(): CardBuy {
        cardClicked = null
        lastActor!!.color = Color.WHITE
        commonModel.playerEvent = PlayerEvent.NONE
        commonModel.setActionCorrect(false)
        return CardBuy(tier, position)
    }


}
