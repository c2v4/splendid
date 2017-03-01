package com.c2v4.splendid.component.reservedcard

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.c2v4.splendid.component.getCard


class ReservedCardsView(val model: ReservedCardsModel, skin: Skin) : Table(skin) {
    val cards: Array<Cell<Actor>?> = kotlin.arrayOfNulls(3)

    init {
        model.cards.forEachIndexed { i, card ->
            cards[i] = add(getCard(card, skin))
            row()
        }
        model.cardChangeObserver.add({ position, card ->
            cards[position]!!.setActor(getCard(card, skin))
        })
    }
}
