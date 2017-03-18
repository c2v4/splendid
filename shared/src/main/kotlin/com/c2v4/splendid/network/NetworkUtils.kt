package com.c2v4.splendid.network

import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Noble
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.network.message.game.*
import com.c2v4.splendid.network.message.login.JoinLobby
import com.c2v4.splendid.network.message.login.LoggedIn
import com.c2v4.splendid.network.message.login.SimpleLogIn
import com.c2v4.splendid.network.message.login.StartGame
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import java.util.*
import kotlin.concurrent.thread

private val CLASSES_TO_REGISTER = listOf(SimpleLogIn::class.java,
        LoggedIn::class.java,
        StartGame::class.java,
        LinkedHashSet::class.java,
        Resource::class.java,
        TakeCoins::class.java,
        HashMap::class.java,
        LinkedHashMap::class.java,
        CardDeal::class.java,
        InitialCoins::class.java,
        NobleDeal::class.java,
        JoinLobby::class.java,
        YourTurn::class.java,
        Card::class.java,
        Noble::class.java)

fun registerClasses(kryo: Kryo) {
    CLASSES_TO_REGISTER.forEach { kryo.register(it) }
}

val GAME_PORT_TCP = 9966
val GAME_PORT_UDP = 9967

fun main(args: Array<String>) {
    val client = Client()
    registerClasses(client.kryo)
    client.start()
//    Log.set(Log.LEVEL_TRACE)
    client.connect(5000, "localhost", GAME_PORT_TCP, GAME_PORT_UDP)
    client.addListener(Listener.ThreadedListener(object : Listener(){
        override fun connected(connection: Connection?) {
            println(connection)
        }

        override fun disconnected(connection: Connection?) {
            println(connection)
            super.disconnected(connection)
        }

        override fun idle(connection: Connection?) {
            super.idle(connection)
        }

        override fun received(connection: Connection?, `object`: Any?) {
//            if( `object` !is FrameworkMessage) {
                println("``object` = ${`object`}")
//            }
        }
    }))
    client.sendTCP(SimpleLogIn("Zygmunt"))
    client.sendTCP(JoinLobby(22))
    println("asd")
    thread { while (true){

    }
    }
}
