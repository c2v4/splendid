package com.c2v4.splendid.core.util

import com.c2v4.splendid.core.model.Resource

fun canBuy(costs: Map<Resource, Int>, wallet: Map<Resource, Int>): Boolean {
    val stillToPay = costs.entries.sumBy {
        Math.max(it.value - wallet.getOrElse(it.key, { 0 }), 0)
    }
    val gold = wallet.getOrElse(Resource.GOLD, { 0 })
    return stillToPay <= gold
}