package com.c2v4.splendid.component.playerstable

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateView

class PlayerTableView(val playerResourceModel: PlayerTableModel, skin:Skin): Table(skin){
   val playerStateView = PlayerStateView(playerResourceModel.player, skin)

    init {
        add(playerStateView)
        row()
        playerResourceModel.players.forEach {
            row()
            add(PlayerStateView(it,skin))
        }
    }

    fun onClick(){

    }
}