package com.c2v4.splendid.network.message.game

import com.c2v4.splendid.core.model.Resource


data class CardReserved(val playerName: String = "",
                        val cardPosition: Int = -1,
                        val tier: Int = -1,
                        val position: Int = -1,
                        val returned: Resource? = null)