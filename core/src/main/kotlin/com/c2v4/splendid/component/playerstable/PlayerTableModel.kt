package com.c2v4.splendid.component.playerstable

import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateModel
import com.c2v4.splendid.core.model.Player

class PlayerTableModel(val name: String,
                       val player: PlayerStateModel,
                       val players: MutableList<PlayerStateModel>) {
    val playerJoinObserver = mutableListOf<(Player) -> Unit>()
    val playerLeftObserver = mutableListOf<(Player) -> Unit>()

    fun getModelForPlayer(player: String): PlayerStateModel {
        if (player == name) return this.player
        val toReturn =players.singleOrNull { it.name==player }
        if (toReturn != null) {
            return toReturn
        }
        throw IllegalStateException()
    }

}