package com.c2v4.splendid.component.cardtable

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.c2v4.splendid.component.getCard
import com.c2v4.splendid.component.getNoble
import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Noble
import com.c2v4.splendid.entity.CardActor
import ktx.actors.alpha
import ktx.actors.onClick

class CardTableView(skin: Skin, model: CardTableModel) : Table(skin) {
    val cardHolders: Array<Array<Cell<Actor>?>> = arrayOf(arrayOfNulls(4),
            arrayOfNulls(4),
            arrayOfNulls(4))
    val nobleHolders: Array<Cell<Actor>?> = arrayOfNulls(Noble.MAX_NOBLES)
    val controllers = mutableListOf<CardTableController>()

    init {
        defaults().center().pad(2f)
        row().padBottom(15f)
        (0..Noble.MAX_NOBLES - 1).forEach {
            nobleHolders[it] = add(com.c2v4.splendid.component.getNoble(model.nobles[it], skin))
        }
        ((Board.NUMBER_OF_TIERS - 1) downTo 0).forEach { i ->
            row()
            add(Image(skin, "card-empty"))
            (0..Board.NUMBER_OF_CARDS_PER_TIER - 1).forEach {
                val card = model.cards[i][it]
                val cardActor = com.c2v4.splendid.component.getCard(card, skin)
                if (card != null) {
                    cardActor.addListener(getClickListener(it, i,cardActor))
                }
                cardHolders[i][it] = add(cardActor)
            }
        }
        model.addCardChangeObserver {
            tier, position, old, new ->
//            val newCardActor = com.c2v4.splendid.component.getCard(new, skin)
//            newCardActor.alpha = 0f
//            val fl = 0.5f
//            cardHolders[tier][position]!!.actor.addAction(Actions.sequence(Actions.fadeOut(fl),
//                    Actions.run { addActor(newCardActor, position, tier) }))
//            newCardActor.addAction(Actions.delay(fl, Actions.fadeIn(fl)))
            addActor(getCard(new, skin), position, tier)
        }
        model.addNobleChangeObserver { noblePosition, oldNoble, newNoble ->
            nobleHolders[noblePosition]!!.setActor(getNoble(newNoble, skin))
        }
    }

    private fun addActor(newCardActor: Actor, position: Int, tier: Int) {
        cardHolders[tier][position]!!.setActor(newCardActor)
        if (newCardActor is CardActor) {
            newCardActor.addListener(getClickListener(position, tier,newCardActor))

            newCardActor.onClick { inputEvent, actor ->
                controllers.forEach {
                    it.click(position,tier,actor)
                }
            }
        }
    }

    private fun getClickListener(position: Int, tier: Int,card:Actor): ClickListener {
        return object : ClickListener() {

            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                super.enter(event, x, y, pointer, fromActor)
                if(card is CardActor){
                    card.showButtons()
                }
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                super.exit(event, x, y, pointer, toActor)
                if(card is CardActor){
                    card.hideButtons()
                }
            }
        }
    }

    fun registerController(cardTableController: CardTableController) {
        controllers.add(cardTableController)
    }
}