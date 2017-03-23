package com.c2v4.splendid.network.message.game

import com.c2v4.splendid.core.model.Resource

data class ReservedCardBought(val position: Int = -1, val playerName:String = "",
                              val toPay: HashMap<Resource, Int> = HashMap()) {
    constructor(position: Int, playerName: String, toPay: Map<Resource, Int>) : this(position,playerName, HashMap(toPay))
}