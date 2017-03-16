package com.c2v4.splendid.network.message.game

import com.c2v4.splendid.core.model.Resource
import java.util.*

data class InitialCoins(val coins: HashMap<Resource, Int> = HashMap()) {
    constructor(coins: Map<Resource, Int>) : this(HashMap(coins))
}