package com.c2v4.splendid.server.network

import com.c2v4.splendid.network.GAME_PORT_TCP
import com.c2v4.splendid.network.GAME_PORT_UDP
import com.c2v4.splendid.network.registerClasses
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import java.util.concurrent.Executors

class MainServer(numberOfThreads:Int,listeners:List<Listener>) {
    val server: Server = Server()

    init {
        registerClasses(server.kryo)
        server.start()
        server.bind(GAME_PORT_TCP, GAME_PORT_UDP)
        val threadPool = Executors.newFixedThreadPool(numberOfThreads)
        listeners.forEach { server.addListener(Listener.ThreadedListener(it,threadPool)) }
    }
}

