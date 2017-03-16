package com.c2v4.splendid.server.network

import com.esotericsoftware.kryonet.Connection
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class ConnectionAssociator {
    companion object {
        val LOGGER = LoggerFactory.getLogger(ConnectionAssociator::class.java)
    }

    private val players = ConcurrentHashMap<Connection, String>()
    private val connections = ConcurrentHashMap<String, Connection>()

    fun disconnect(connection: Connection?) {
        val player = players.remove(connection)
        if (player != null) {
            connections.remove(player)
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
    fun getPlayer(connection: Connection): String = players.getOrElse(connection,
            { throw IllegalArgumentException("Player not connected") })
}