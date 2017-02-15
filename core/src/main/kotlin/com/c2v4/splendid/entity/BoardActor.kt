package com.c2v4.splendid.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Noble
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.manager.FontManager

class BoardActor(skin: Skin) : Table(skin) {
    val cardHolders: List<MutableList<Cell<Actor>>> = listOf(mutableListOf(), mutableListOf(), mutableListOf())
    val nobleHolders: MutableList<Cell<Actor>> = mutableListOf()


    init {

        val cardTable = initializeCardTable(skin)
        defaults().expand()
        add(cardTable)
        add(ResourceTable(skin))
        add(PlayersTable(skin))
        add(ReservedCardsTable(skin))
        debugAll()
        pack()

        touchable = Touchable.childrenOnly
    }

    private fun initializeCardTable(skin: Skin): Table {
        val cardTable = Table()
        cardTable.defaults().center().pad(5f)
        cardTable.row().padBottom(25f)
        (0..Noble.MAX_NOBLES - 1).forEach {
            nobleHolders.add(cardTable.add(Image(skin, "card-empty")))
        }
        (0..Board.NUMBER_OF_TIERS - 1).forEach { i ->
            cardTable.row()
            cardTable.add(Image(skin, "card-empty"))
            (0..Board.NUMBER_OF_CARDS_PER_TIER - 1).forEach {
                cardHolders[Board.NUMBER_OF_TIERS - i - 1].add(cardTable.add(Image(skin, "card-empty")))
            }
        }
        cardTable.pack()
        return cardTable
    }

}

class ReservedCardsTable(skin: Skin):Table(skin){
    init {
        add(Image(skin, "card-empty"))
        row()
        add(Image(skin, "card-empty"))
        row()
        add(Image(skin, "card-empty"))
    }
}


class ResourceTable(skin: Skin, vertical: Boolean = true) : Table(skin) {
    val amounts = Resource.values().map { it to Label("0", skin, FontManager.UI_FONT, Color.WHITE) }.toMap()

    init {
        defaults().pad(5f)
        amounts.forEach {
            if (vertical) row()
            add(Image(skin, "gem-${it.key.name.toLowerCase()}"))
            add(it.value)
        }
        pack()
    }

    fun setAmount(resource: Resource, amount: Int) {
        amounts[resource]!!.setText("$amount")
    }
}

class PlayersTable(skin: Skin):Table(skin) {
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
    val  wallet = Resource.values().map { it to Label("0", skin, FontManager.UI_FONT, Color.WHITE) }.toMap()
    val cards = Resource.values().filter { it!=Resource.GOLD }.map { it to Label("0", skin, FontManager.UI_FONT, Color.WHITE) }.toMap()
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
        add(Image(skin,"gem-red"))
        wallet.values.forEach { add(it) }
        add(walletSum).padLeft(10f)
        row()
        add(Image(skin,"icon/cards"))
        cards.values.forEach { add(it) }
        add()
        add(reservedCards).padLeft(10f)
        row()
        add(Image(skin,"gem-gold"))
        sums.values.forEach { add(it) }
        pack()

        setCardsAmount(Resource.RED,3)
        setWalletAmount(Resource.RED,1)
        setWalletAmount(Resource.BLUE,4)
        setWalletAmount(Resource.BLACK,6)
        setWalletAmount(Resource.BLACK,3)
    }

    fun setCardsAmount(resource: Resource, amount: Int) {
        setAmount(cards,resource,amount)
    }

    fun setWalletAmount(resource: Resource, amount: Int) {
       setAmount(wallet,resource,amount)
    }

    private fun setAmount(collection: Map<Resource, Label>, resource: Resource, amount: Int) {
        val label = collection[resource]
        val previousAmount = label!!.text.toString().toInt()
        label.setText("$amount")
        val sumLabel = sums[resource]
        val newSum = sumLabel!!.text.toString().toInt()-previousAmount+amount
        sumLabel!!.setText("$newSum")
        val totalSum = wallet.values.sumBy { it.text.toString().toInt() }
        walletSum.setText("$totalSum")
    }

}
