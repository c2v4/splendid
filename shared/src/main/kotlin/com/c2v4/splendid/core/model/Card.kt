package com.c2v4.splendid.core.model

data class Card(val tier: Int, val points: Int, val costs: Map<Resource, Int>, val resource: Resource) :Buyable {
    override fun canBuy(wallet: Map<Resource, Int>):Boolean {
        return canBuy(wallet,costs)
    }
}
