package com.c2v4.splendid.server.network

import com.c2v4.splendid.network.GAME_PORT_TCP
import com.c2v4.splendid.network.GAME_PORT_UDP
import com.c2v4.splendid.network.registerClasses
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.FrameworkMessage
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import java.util.concurrent.Executors

class MainServer(numberOfThreads:Int,listeners:List<Listener>) {

    val server: Server = object :Server(){
        override fun newConnection(): Connection {
            val LOG = org.slf4j.LoggerFactory.getLogger(Connection::class.java)
            return object :Connection(){
                override fun sendUDP(p0: Any?): Int {
                    if(p0 !is FrameworkMessage.KeepAlive) {
                        LOG.debug("Sent: {} to {}", p0, this)
                    }
                    return super.sendUDP(p0)
                }

                override fun sendTCP(p0: Any?): Int {
                    if(p0 !is FrameworkMessage.KeepAlive) {
                        LOG.debug("Sent: {} to {}", p0, this)
                    }
                    return super.sendTCP(p0)
                }
            }
        }
    }

    init {
        registerClasses(server.kryo)
        server.start()
        server.bind(GAME_PORT_TCP, GAME_PORT_UDP)
        val threadPool = Executors.newFixedThreadPool(numberOfThreads)
        listeners.forEach { server.addListener(Listener.ThreadedListener(it,threadPool)) }
    }
}

