package com.c2v4.splendid.core.model

import com.c2v4.splendid.core.util.canBuy
import java.util.*

data class Card(val tier: Int=-1, val points: Int=-1, val cost: Map<Resource, Int> = HashMap(), val resource: Resource=Resource.GOLD)  {

     fun canBuy(wallet: Map<Resource, Int>):Boolean {
        return canBuy(cost, wallet)
    }
}
