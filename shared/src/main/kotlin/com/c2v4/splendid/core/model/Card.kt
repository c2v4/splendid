package com.c2v4.splendid.core.model

import com.c2v4.splendid.core.util.canBuy

data class Card(val tier: Int, val points: Int, val costs: Map<Resource, Int>, val resource: Resource)  {

     fun canBuy(wallet: Map<Resource, Int>):Boolean {
        return canBuy(costs, wallet)
    }
}
