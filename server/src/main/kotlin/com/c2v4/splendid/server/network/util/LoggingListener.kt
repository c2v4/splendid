package com.c2v4.splendid.server.network.util

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.FrameworkMessage
import com.esotericsoftware.kryonet.Listener
import org.slf4j.LoggerFactory

class LoggingListener : Listener() {
    companion object {
        val LOGGER = LoggerFactory.getLogger(LoggingListener::class.java)!!
    }

    override fun connected(connection: Connection?) {
        LOGGER.debug("Connected: {}", connection)
    }

    override fun disconnected(connection: Connection?) {
        LOGGER.debug("Disconnected: {}", connection)
    }

    override fun idle(connection: Connection?) {
        LOGGER.trace("Idle: {}", connection)
    }

    override fun received(connection: Connection?, `object`: Any?) {
        if (`object` !is FrameworkMessage) {
            LOGGER.info("Received: {} form {}", `object`, connection)
        }
    }
}