package com.c2v4.splendid.cardtable

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Noble
import ktx.actors.alpha

class CardTableView(skin: Skin, model: CardTableModel) : Table(skin) {
    val cardHolders: List<MutableList<Cell<Actor>>> = listOf(mutableListOf(),
            mutableListOf(),
            mutableListOf())
    val nobleHolders: MutableList<Cell<Actor>> = mutableListOf()


    init {
        defaults().center().pad(5f)
        row().padBottom(25f)
        (0..Noble.MAX_NOBLES - 1).forEach {
            nobleHolders.add(add(getNoble(model.nobles[it], skin)))
        }
        ((Board.NUMBER_OF_TIERS - 1) downTo 0).forEach { i ->
            row()
            add(Image(skin, "card-empty"))
            (0..Board.NUMBER_OF_CARDS_PER_TIER - 1).forEach {
                cardHolders[i].add(add(getCard(model.cards[i][it], skin)))
            }
        }
        model.addCardChangeObserver {
            tier, position, old, new ->
            val newCardActor = getCard(new, skin)
            newCardActor.alpha = 0f
            val fl = 0.5f
            cardHolders[tier][position].actor.addAction(Actions.sequence(Actions.fadeOut(fl),
                    Actions.run { cardHolders[tier][position].setActor(newCardActor) }))
            newCardActor.addAction(Actions.delay(fl, Actions.fadeIn(fl)))
        }
    }
}