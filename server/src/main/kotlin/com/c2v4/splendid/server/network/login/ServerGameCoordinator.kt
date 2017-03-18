package com.c2v4.splendid.server.network.login

import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Noble
import com.c2v4.splendid.core.model.Player
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.gateway.GameCoordinator
import com.c2v4.splendid.network.message.game.CardDeal
import com.c2v4.splendid.network.message.game.InitialCoins
import com.c2v4.splendid.network.message.game.NobleDeal
import com.c2v4.splendid.network.message.game.YourTurn
import com.c2v4.splendid.network.message.login.StartGame
import com.esotericsoftware.kryonet.Connection

class ServerGameCoordinator(val playersToConnections: MutableMap<Player, Connection>) : GameCoordinator {
    override fun sendStartGameEvent(lobby: Set<String>) {
        sendToEveryone(StartGame(lobby))
    }

    override fun cardDealt(tier: Int, position: Int, card: Card) {
        sendToEveryone(CardDeal(tier, position, card))
    }


    override fun nobleDealt(position: Int, noble: Noble) {
        sendToEveryone(NobleDeal(position, noble))
    }

    override fun coinsTaken(player: Player, taken: Map<Resource, Int>) {

    }

    override fun setCurrentPlayer(currentPlayer: Player) {
        sendToPlayer(currentPlayer, YourTurn())
    }

    fun sendInitialState(availableCoins: Map<Resource, Int>) {
        sendToEveryone(InitialCoins(availableCoins))
    }

    private fun sendToEveryone(toSend: Any?) {
        playersToConnections.values.forEach { it.sendTCP(toSend) }
    }

    private fun sendToPlayer(player: Player, toSend: Any) {
        playersToConnections[player]!!.sendTCP(toSend)
    }

}