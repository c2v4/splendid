package com.c2v4.splendid.core.model

class Player(val cardsReserved: Array<Card?> = arrayOfNulls<Card>(3),
             var wallet: Map<Resource, Int> = mutableMapOf(),
             var resourcesFromCards: Map<Resource, Int> = mutableMapOf(),
             var points: Int = 0) {


}