package com.c2v4.splendid.component.playerstable.playersttate

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.c2v4.splendid.component.resourcetable.ClickableResource
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.manager.FontManager
import ktx.actors.onClick

class PlayerStateView(val model: PlayerStateModel, skin: Skin) : Table(skin) {
    val wallet = Resource.values().map {
        it to Label("0",
                skin,
                FontManager.UI_FONT,
                Color.WHITE)
    }.toMap()
    val cards = Resource.values().filter { it != Resource.GOLD }.map {
        it to Label("0",
                skin,
                FontManager.UI_FONT,
                Color.WHITE)
    }.toMap()
    val sums = Resource.values().map {
        it to Label("0",
                skin,
                FontManager.UI_FONT,
                Color.WHITE)
    }.toMap()
    val reservedCards = Label("0", skin, FontManager.UI_FONT, Color.WHITE)
    val walletSum = Label("0", skin, FontManager.UI_FONT, Color.WHITE)
    val pointLabel = Label("0", skin, FontManager.UI_FONT, Color.WHITE)
    val gems = Resource.values().map { it to ClickableResource(skin, it) }.toMap()

    init {
        defaults().padLeft(5f).padRight(5f)
        if (model.displayName) {
            add(model.name, FontManager.PLAYER_NAME_FONT, Color.WHITE).colspan(Resource.values().size+2)
            row()
        }
        add()
        Resource.values().forEach {
            add(gems[it])
        }
        row()
        add(Image(skin, "gem/red"))
        wallet.forEach {
            add(it.value)
            it.value.setText("${model.wallet.getOrElse(it.key, { 0 })}")
        }
        add(walletSum).padLeft(10f)
        walletSum.setText("${model.wallet.values.sum()}")
        row()
        add(Image(skin, "icon/cards"))
        cards.forEach {
            add(it.value)
            it.value.setText("${model.cards.getOrElse(it.key, { 0 })}")
        }
        add()
        add(reservedCards).padLeft(10f)
        reservedCards.setText("${model.cardsReserved}")
        row()
        add(Image(skin, "gem/gold"))
        sums.forEach {
            add(it.value)
            it.value.setText("${model.wallet.getOrElse(it.key,
                    { 0 }) + model.cards.getOrElse(it.key, { 0 })}")
        }
        add(pointLabel).padLeft(10f)

        model.cardChangeObs.add { resource, amount ->
            cards[resource]!!.setText("$amount")
            sums[resource]!!.setText("${amount + model.wallet.getOrElse(resource, { 0 })}")
        }
        model.cardsReservedObs.add { amount -> reservedCards.setText("${amount}") }
        model.walletChangeObs.add { resource, amount ->
            wallet[resource]!!.setText("$amount")
            sums[resource]!!.setText("${amount + model.cards.getOrElse(resource, { 0 })}")
            walletSum.setText("${model.wallet.filterKeys { it != resource }.values.sum() + amount}")
        }
        model.pointsObs.add { amount -> pointLabel.setText("$amount") }

        pack()
    }

    fun onClick(resource: Resource,listener: (InputEvent, ClickableResource) -> Unit){
        println("Listenet added: "+resource)
        gems[resource]!!.onClick(listener)
    }

}