package com.c2v4.splendid.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.c2v4.splendid.component.CommonModel
import com.c2v4.splendid.component.cardtable.CardTableView
import com.c2v4.splendid.component.playerstable.PlayerTableView
import com.c2v4.splendid.component.reservedcard.ReservedCardsView
import com.c2v4.splendid.component.resourcetable.ResourceTableView
import com.c2v4.splendid.manager.FontManager
import ktx.actors.onClick

class BoardView(skin: Skin,
                cardTable: CardTableView,
                resourceView: ResourceTableView,
                playerTableView: PlayerTableView,
                reservedCardsView: ReservedCardsView,
                model: CommonModel) : Table(skin) {

    private val button= Button(skin)

    init {
        defaults().expand()
        add(cardTable)
        val table = Table()
        table.add(resourceView)
        table.row()
        button.add("GO", FontManager.UI_FONT, Color.WHITE)
        button.touchable=Touchable.disabled
        model.addActionCorrectListener { actionCorrect ->
            if (actionCorrect) {
                button.touchable = Touchable.enabled
            } else {
                button.touchable = Touchable.disabled
            }
        }
        table.add(button)
        add(table)
        add(playerTableView)
        add(reservedCardsView)
        pack()
        model.addTurnListener({ isPlayerTurn ->
            println(isPlayerTurn)
            if (isPlayerTurn) {
                touchable = Touchable.childrenOnly
            } else {
                touchable = Touchable.disabled
            }
        })
        touchable = Touchable.disabled
    }

    fun onButtonClick(listener: (InputEvent, Button) -> Unit ){
        button.onClick(listener)
    }


}
