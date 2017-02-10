package com.c2v4.splendid.core.model

import com.c2v4.splendid.core.model.Resource.GOLD

class Card(val tier: Int, val points: Int, val costs: Map<Resource, Int>, val resource: Resource) {

    fun canBuy(wallet: Map<Resource, Int>): Boolean {
        val stillToPay = costs.entries.sumBy {
            Math.max(it.value-wallet.getOrElse(it.key,{0}) ,0)
        }
        val gold = wallet.getOrElse(GOLD,{0})
        return stillToPay <= gold
    }

}
