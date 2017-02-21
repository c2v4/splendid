package com.c2v4.splendid.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.c2v4.splendid.component.cardtable.CardTableView
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateModel
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateView
import com.c2v4.splendid.component.resourcetable.ResourceTableView
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.manager.FontManager
import ktx.actors.onClick

class BoardView(skin: Skin, cardTable: CardTableView, resourceView: ResourceTableView,playerStateView: PlayerStateView) : Table(skin) {

    init {
        defaults().expand()
        add(cardTable)
        add(resourceView)
        add(playerStateView)
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


//class PlayersTable(skin: Skin) : Table(skin) {
//    init {
//        add(PlayerResourceTable(skin))
//        row()
//        add(PlayerResourceTable(skin))
//        row()
//        add(PlayerResourceTable(skin))
//        row()
//        add(PlayerResourceTable(skin))
//        row()
//    }
//}


class ClickableResource(skin: Skin, val resource: Resource) : Table(skin) {
    val label = Label("", skin, FontManager.UI_FONT, Color.WHITE)

    init {
        setBackground("gem/${resource.name.toLowerCase()}")
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