package com.c2v4.splendid.network.message.game

import com.c2v4.splendid.core.model.Resource

data class ReserveCard(val tier: Int = -1,
                       val position: Int = -1,
                       val returned: HashMap<Resource, Int> = HashMap()) {
    constructor(tier: Int, position: Int, returned: Map<Resource, Int>) : this(tier,
            position,
            java.util.HashMap(returned))
}