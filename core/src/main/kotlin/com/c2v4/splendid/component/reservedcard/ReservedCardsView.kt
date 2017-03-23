package com.c2v4.splendid.component.reservedcard

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.c2v4.splendid.component.getCard
import com.c2v4.splendid.entity.CardActor
import ktx.actors.onClick


class ReservedCardsView(val model: ReservedCardsModel, skin: Skin) : Table(skin) {
    val cards: Array<Cell<Actor>?> = kotlin.arrayOfNulls(3)
    val onClickListeners: MutableSet<(position: Int, card: Actor) -> Unit> = mutableSetOf()

    init {
        model.cards.forEachIndexed { i, card ->
            addCard(i, getCard(card,skin))
            row()
        }
        model.cardChangeObserver.add({ position, card ->
            addCard(position, getCard(card,skin))
        })
    }

    fun addCard(position: Int, card: Actor) {
        if (cards[position] == null) {
            cards[position] = add(card)
        } else {
            cards[position]!!.setActor(card)
        }
        card.onClick { _, cardActor ->
            onClickListeners.forEach {
                it.invoke(position, cardActor)
            }
        }
    }

}
