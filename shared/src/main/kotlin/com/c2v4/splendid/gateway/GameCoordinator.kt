package com.c2v4.splendid.gateway

import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Noble
import com.c2v4.splendid.core.model.Player
import com.c2v4.splendid.core.model.Resource

interface GameCoordinator {
    fun setCurrentPlayer(currentPlayer: Player)
    fun coinsTaken(player: Player,
                   taken: Map<Resource, Int>,
                   boardsCoinsAvailable: Map<Resource, Int>)

    fun cardDealt(tier: Int, position: Int, card: Card)
    fun nobleDealt(position: Int, noble: Noble)
    fun sendStartGameEvent(lobby: Set<String>)
    fun cardReserved(player: Player,
                     cardPosition: Int,
                     tier: Int,
                     position: Int,
                     returned: Resource?)

    fun cardBought(player: Player, tier: Int, position: Int, toPay: Map<Resource, Int>)
    fun nobleTaken(player: Player, position: Int)
    fun reservedCardBought(position: Int,player:Player,card: Card, toPay: Map<Resource, Int>)

}