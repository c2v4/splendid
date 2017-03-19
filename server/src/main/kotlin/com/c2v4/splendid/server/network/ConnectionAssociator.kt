package com.c2v4.splendid.server.network

import com.c2v4.splendid.core.model.Player
import com.esotericsoftware.kryonet.Connection
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class ConnectionAssociator {
    companion object {
        val LOGGER = LoggerFactory.getLogger(ConnectionAssociator::class.java)
    }

    private val players = ConcurrentHashMap<Connection, String>()
    private val connections = ConcurrentHashMap<String, Connection>()
    private val playerNamesToPlayers = ConcurrentHashMap<String, Player>()
    private val playerToPlayerNames = ConcurrentHashMap<Player, String>()

    fun disconnect(connection: Connection?) {
        val player = players.remove(connection)
        if (player != null) {
            connections.remove(player)
            playerNamesToPlayers.remove(player)
        }
    }

    fun getConnection(player: String): Connection = connections.getOrElse(player, {
        LOGGER.warn("Player {} disconnected", player)
        throw IllegalStateException()
    })

    fun isPlayerLogged(login: String): Boolean = connections.containsKey(login)
    fun associate(connection: Connection, login: String) {
        players.put(connection,login)
        connections.put(login, connection)
    }

    fun isPlayerLogged(connection: Connection): Boolean = players.containsKey(connection)
    fun getPlayerName(connection: Connection): String = players.getOrElse(connection,
            { throw IllegalArgumentException("Player not connected") })

    fun associate(playerName: String, player: Player) {
        playerNamesToPlayers.put(playerName,player)
        playerToPlayerNames.put(player,playerName)
    }

    fun getPlayer(connection: Connection): Player {
        val s = players.getOrElse(connection,{throw IllegalStateException()})
        return playerNamesToPlayers.getOrElse(s,{throw IllegalStateException()})
    }

    fun getPlayerName(player: Player): String {
        return playerToPlayerNames.getOrElse(player,{throw IllegalStateException()})
    }
}