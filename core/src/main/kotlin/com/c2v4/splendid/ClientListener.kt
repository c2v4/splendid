package com.c2v4.splendid

import com.c2v4.splendid.network.message.game.*
import com.c2v4.splendid.network.message.login.StartGame
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener

class ClientListener(val clientController: ClientController) : Listener() {

    override fun connected(connection: Connection) {
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
                is PlayerTurn -> clientController.setPlayerTurn(received)
                is CoinsTaken -> clientController.coinsTaken(received)
                is CardReserved -> clientController.cardReserved(received)
                is CardBought -> clientController.cardBought(received)
                is NobleTaken -> clientController.nobleTaken(received)
            }
            return Unit
        })
    }


    fun setClient(client: Client) {
        clientController.setClient(client)
    }
}