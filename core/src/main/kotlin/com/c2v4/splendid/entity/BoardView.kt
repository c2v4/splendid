package com.c2v4.splendid.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.c2v4.splendid.cardtable.CardTableView
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.manager.FontManager
import ktx.actors.onClick

class BoardView(skin: Skin, cardTable: CardTableView) : Table(skin) {

    init {
        defaults().expand()
        add(cardTable)
//        add(ResourceTable(skin, ResourceTableModel()))
//        add(PlayersTable(skin))
//        add(ReservedCardsTable(skin))
//        debugAll()
        pack()

        touchable = Touchable.childrenOnly
    }


}

class ReservedCardsTable(skin: Skin) : Table(skin) {
    init {
        add(Image(skin, "card-empty"))
        row()
        add(Image(skin, "card-empty"))
        row()
        add(Image(skin, "card-empty"))
    }
}


class PlayersTable(skin: Skin) : Table(skin) {
    init {
        add(PlayerResourceTable(skin))
        row()
        add(PlayerResourceTable(skin))
        row()
        add(PlayerResourceTable(skin))
        row()
        add(PlayerResourceTable(skin))
        row()
    }
}

class PlayerResourceTable(skin: Skin) : Table(skin) {
    val wallet = Resource.values().map { it to Label("0", skin, FontManager.UI_FONT, Color.WHITE) }.toMap()
    val cards = Resource.values().filter { it != Resource.GOLD }.map { it to Label("0", skin, FontManager.UI_FONT, Color.WHITE) }.toMap()
    val sums = Resource.values().map { it to Label("0", skin, FontManager.UI_FONT, Color.WHITE) }.toMap()
    val reservedCards = Label("0", skin, FontManager.UI_FONT, Color.WHITE)
    val walletSum = Label("0", skin, FontManager.UI_FONT, Color.WHITE)

    init {
        defaults().padLeft(5f).padRight(5f)
        add()
        Resource.values().forEach {
            add(Image(skin, "gem-${it.name.toLowerCase()}"))
        }
        row()
        add(Image(skin, "gem-red"))
        wallet.values.forEach { add(it) }
        add(walletSum).padLeft(10f)
        row()
        add(Image(skin, "icon/cards"))
        cards.values.forEach { add(it) }
        add()
        add(reservedCards).padLeft(10f)
        row()
        add(Image(skin, "gem-gold"))
        sums.values.forEach { add(it) }
        pack()

        setCardsAmount(Resource.RED, 3)
        setWalletAmount(Resource.RED, 1)
        setWalletAmount(Resource.BLUE, 4)
        setWalletAmount(Resource.BLACK, 6)
        setWalletAmount(Resource.BLACK, 3)
    }

    fun setCardsAmount(resource: Resource, amount: Int) {
        setAmount(cards, resource, amount)
    }

    fun setWalletAmount(resource: Resource, amount: Int) {
        setAmount(wallet, resource, amount)
    }

    private fun setAmount(collection: Map<Resource, Label>, resource: Resource, amount: Int) {
        val label = collection[resource]
        val previousAmount = label!!.text.toString().toInt()
        label.setText("$amount")
        val sumLabel = sums[resource]
        val newSum = sumLabel!!.text.toString().toInt() - previousAmount + amount
        sumLabel.setText("$newSum")
        val totalSum = wallet.values.sumBy { it.text.toString().toInt() }
        walletSum.setText("$totalSum")
    }

}

class ClickableResource(skin: Skin, val resource: Resource) : Table(skin) {
    val label = Label("", skin, FontManager.UI_FONT, Color.WHITE)

    init {
        setBackground("gem-${resource.name.toLowerCase()}")
        right().bottom()
        add(label)
        touchable = Touchable.enabled
    }

    fun setOnClick(listener: (InputEvent, ClickableResource) -> Unit) {
        onClick(listener)
    }

    fun setChosenAmount(i: Int) {
        label.setText("$i")
    }
}