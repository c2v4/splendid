package com.c2v4.splendid.network.message.game

import com.c2v4.splendid.core.model.Resource

data class CardBought(val playerName: String = "",
                      val tier: Int = -1,
                      val position: Int = -1,
                      val toPay: HashMap<Resource, Int> = HashMap()) {
    constructor(playerName: String, tier: Int, position: Int, toPay: Map<Resource, Int>) : this(
            playerName,
            tier,
            position,
            HashMap(toPay))
}