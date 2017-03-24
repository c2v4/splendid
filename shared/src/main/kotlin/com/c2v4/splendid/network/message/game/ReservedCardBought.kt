package com.c2v4.splendid.network.message.game

import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Resource

data class ReservedCardBought(val position: Int = -1, val playerName:String = "", val card: Card? = null,
                              val toPay: HashMap<Resource, Int> = HashMap()) {
    constructor(position: Int, playerName: String,card: Card?, toPay: Map<Resource, Int>) : this(position,playerName,card,HashMap(toPay))
}