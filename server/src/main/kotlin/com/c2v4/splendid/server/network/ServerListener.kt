package com.c2v4.splendid.server.network

import com.c2v4.splendid.network.message.game.ReserveCard
import com.c2v4.splendid.network.message.game.TakeCoins
import com.c2v4.splendid.network.message.login.JoinLobby
import com.c2v4.splendid.network.message.login.LoggedIn
import com.c2v4.splendid.network.message.login.SimpleLogIn
import com.c2v4.splendid.server.network.login.GameService
import com.c2v4.splendid.server.network.login.LobbyService
import com.c2v4.splendid.server.network.login.LogInService
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import org.slf4j.LoggerFactory

class ServerListener(val associator: ConnectionAssociator,
                     val logInService: LogInService,
                     val lobbyService: LobbyService,
                     val gameService: GameService) : Listener() {
    companion object {
        val LOGGER = LoggerFactory.getLogger(ServerListener::class.java)!!
    }

    override fun connected(connection: Connection) {

    }

    override fun disconnected(connection: Connection) {
        associator.disconnect(connection)
    }

    override fun received(connection: Connection, received: Any?) {
        synchronized(connection, {
            when (received) {
                is SimpleLogIn -> {
                    if (associator.isPlayerLogged(received.login)) {
                        connection.sendUDP(LoggedIn(false))
                    } else {
                        associator.associate(connection, received.login)
                        connection.sendUDP(LoggedIn(true))
                    }
                }
                is JoinLobby -> {
                    if (associator.isPlayerLogged(connection)) {
                        lobbyService.join(associator.getPlayerName(connection), received.lobbyId)
                    } else {
                        throw IllegalStateException()
                    }
                }
                is TakeCoins -> {
                    gameService.takeCoins(connection,received)
                }
                is ReserveCard ->{
                    gameService.reserveCard(connection,received)
                }
            }
            return Unit
        })
    }
}