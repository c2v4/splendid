package com.c2v4.splendid.entity

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.c2v4.splendid.component.CommonModel
import com.c2v4.splendid.component.cardtable.CardTableView
import com.c2v4.splendid.component.playerstable.PlayerTableView
import com.c2v4.splendid.component.reservedcard.ReservedCardsView
import com.c2v4.splendid.component.resourcetable.ResourceTableView

class BoardView(skin: Skin,
                cardTable: CardTableView,
                resourceView: ResourceTableView,
                playerTableView: PlayerTableView,
                reservedCardsView: ReservedCardsView,
                model: CommonModel) : Table(skin) {

    init {
        defaults().expand()
        add(cardTable)
        add(resourceView)
        add(playerTableView)
        add(reservedCardsView)
        pack()
        model.addTurnListener({ isPlayerTurn ->
            if (isPlayerTurn) {
                touchable = Touchable.childrenOnly
            } else {
                touchable = Touchable.disabled
            }
        })
        touchable = Touchable.disabled
    }


}
