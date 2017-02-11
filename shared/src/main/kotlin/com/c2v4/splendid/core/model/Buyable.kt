package com.c2v4.splendid.core.model

interface Buyable{

    fun canBuy(wallet: Map<Resource, Int>):Boolean

    fun canBuy(wallet: Map<Resource, Int>, cost:Map<Resource,Int>): Boolean {
        val stillToPay = cost.entries.sumBy {
            Math.max(it.value-wallet.getOrElse(it.key,{0}) ,0)
        }
        val gold = wallet.getOrElse(Resource.GOLD,{0})
        return stillToPay <= gold
    }
}