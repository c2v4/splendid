package com.c2v4.splendid

import com.c2v4.splendid.network.message.game.CardDeal
import com.c2v4.splendid.network.message.game.InitialCoins
import com.c2v4.splendid.network.message.game.NobleDeal
import com.c2v4.splendid.network.message.game.YourTurn
import com.c2v4.splendid.network.message.login.StartGame
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener

class ClientListener(val clientController: ClientController) : Listener() {
    override fun connected(connection: Connection?) {
        super.connected(connection)
    }

    override fun disconnected(connection: Connection?) {
        super.disconnected(connection)
    }

    override fun idle(connection: Connection?) {
        super.idle(connection)
    }

    override fun received(connection: Connection, received: Any) {
        synchronized(connection, {
            when (received) {
                is StartGame -> clientController.startGame(received.players)
                is InitialCoins -> clientController.setInitialCoins(received)
                is CardDeal -> clientController.cardDeal(received)
                is NobleDeal -> clientController.nobleDeal(received)
                is YourTurn -> clientController.setPlayerTurn(true)
            }
            return Unit
        })
    }
}