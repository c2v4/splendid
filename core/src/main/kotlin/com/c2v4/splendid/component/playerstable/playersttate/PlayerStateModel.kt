package com.c2v4.splendid.component.playerstable.playersttate

import com.c2v4.splendid.core.model.Resource
import java.util.*


class PlayerStateModel(val wallet: MutableMap<Resource, Int>, val cards: MutableMap<Resource, Int>, var cardsReserved: Int) {
    val walletChangeObs = mutableListOf<(resource:Resource,amount:Int)->Unit>()
    val cardChangeObs = mutableListOf<(resource:Resource,amount:Int)->Unit>()
    val cardsReservedObs = mutableListOf<(amount:Int)->Unit>()

    companion object {
        fun empty(): PlayerStateModel {
            return PlayerStateModel(HashMap((Resource.values().map { it to 0 }).toMap()),
                    HashMap((Resource.values().filter { it != Resource.GOLD }.map { it to 0 }).toMap()),
                    0)
        }
    }

    fun setCardAmount(resource: Resource,amount:Int){
        cardChangeObs.forEach { it.invoke(resource,amount) }
        cards[resource] = amount
    }

    fun setWalletAmount(resource: Resource,amount:Int){
        walletChangeObs.forEach { it.invoke(resource,amount) }
        wallet[resource] = amount
    }

    fun setCardsReservedAmount(amount:Int){
        cardsReservedObs.forEach { it.invoke(amount) }
        cardsReserved= amount
    }

}