package com.c2v4.splendid.component.cardtable

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Noble
import ktx.actors.alpha

class CardTableView(skin: Skin, model: CardTableModel) : Table(skin) {
    val cardHolders: Array<Array<Cell<Actor>?>> = arrayOf(arrayOfNulls(4),
            arrayOfNulls(4),
            arrayOfNulls(4))
    val nobleHolders: Array<Cell<Actor>?> = arrayOfNulls(Noble.MAX_NOBLES)
    val controllers = mutableListOf<CardTableController>()

    init {
        defaults().center().pad(5f)
        row().padBottom(25f)
        (0..Noble.MAX_NOBLES - 1).forEach {
            nobleHolders[it] = add(getNoble(model.nobles[it], skin))
        }
        ((Board.NUMBER_OF_TIERS - 1) downTo 0).forEach { i ->
            row()
            add(Image(skin, "card-empty"))
            (0..Board.NUMBER_OF_CARDS_PER_TIER - 1).forEach {
                val card = getCard(model.cards[i][it], skin)
                card.addListener(getClickListener(it,i))
                cardHolders[i][it] = add(card)
            }
        }
        model.addCardChangeObserver {
            tier, position, old, new ->
            val newCardActor = getCard(new, skin)
            newCardActor.alpha = 0f
            val fl = 0.5f
            cardHolders[tier][position]!!.actor.addAction(Actions.sequence(Actions.fadeOut(fl),
                    Actions.run { addActor(newCardActor, position, tier) }))
            newCardActor.addAction(Actions.delay(fl, Actions.fadeIn(fl)))
        }
    }

    private fun addActor(newCardActor: Actor, position: Int, tier: Int) {
        cardHolders[tier][position]!!.setActor(newCardActor)
        newCardActor.addListener(getClickListener(position,tier))
    }

    private fun getClickListener(position: Int, tier: Int): ClickListener {
        return object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                controllers.forEach { it.clickedCard(tier,position) }
            }
        }
    }

    fun registerController(cardTableController: CardTableController) {
        controllers.add(cardTableController)
    }
}