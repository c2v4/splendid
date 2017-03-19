package com.c2v4.splendid.network.message.game

import com.c2v4.splendid.core.model.Resource

data class CoinsTaken(val player: String = "",
                      val balance: HashMap<Resource, Int> = HashMap(),
                      val boardsCoinsAvailable: HashMap<Resource, Int> = HashMap()) {
    constructor(player: String, taken: Map<Resource, Int>, boardsCoinsAvailable: Map<Resource, Int>) : this(
            player,
            HashMap(taken),
            HashMap(boardsCoinsAvailable))
}