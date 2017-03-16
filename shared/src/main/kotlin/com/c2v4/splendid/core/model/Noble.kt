package com.c2v4.splendid.core.model

import com.c2v4.splendid.core.util.canBuy
import java.util.*

data class Noble(val resources: Map<Resource, Int> = HashMap()){
    fun canBuy(wallet: Map<Resource, Int>): Boolean {
        return canBuy(resources, wallet)
    }

    companion object {
        val POINTS_FOR_NOBLE = 3
        val MAX_NOBLES = 5
    }
}
