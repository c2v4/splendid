package com.c2v4.splendid.core.model

data class Noble(val resources:Map<Resource,Int>):Buyable{
    override fun canBuy(wallet: Map<Resource, Int>):Boolean {
        return canBuy(wallet,resources)
    }

    companion object{
        val POINTS_FOR_NOBLE = 3
    }
}
