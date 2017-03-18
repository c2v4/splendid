package com.c2v4.splendid.network

import com.c2v4.splendid.ClientListener
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Listener
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

class NetworkAdapter(commenceListener: ClientListener, val address: String = "localhost") {

    val client = Client()
    val threadPool = Executors.newFixedThreadPool(2,DaemonFactory())

    init {
        registerClasses(client.kryo)
        connect()
        addListener(commenceListener)
    }

    fun connect(): Unit {
        client.start()
        client.connect(5000, address, GAME_PORT_TCP, GAME_PORT_UDP)
    }

    fun send(toSend: Any) {
        client.sendTCP(toSend)
    }

    fun addListener(listener: Listener) {
        client.addListener(Listener.ThreadedListener(listener, threadPool))
    }
}

class DaemonFactory:ThreadFactory{
    override fun newThread(r: Runnable?): Thread {
        val thread = Executors.defaultThreadFactory().newThread(r)
        thread.isDaemon = true
        return thread
    }

}