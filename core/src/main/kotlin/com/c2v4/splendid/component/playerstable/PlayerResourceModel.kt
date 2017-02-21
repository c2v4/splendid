package com.c2v4.splendid.component.playerstable

import com.c2v4.splendid.core.model.Player

class PlayerResourceModel(val player:Player,val players: MutableMap<String,Player>){
    val playerJoinObserver = mutableListOf<(Player)->Unit>()
    val playerLeftObserver = mutableListOf<(Player)->Unit>()


}