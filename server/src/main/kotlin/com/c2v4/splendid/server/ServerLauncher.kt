package com.c2v4.splendid.server

import com.c2v4.splendid.server.data.InitialDataLoader
import com.c2v4.splendid.server.network.ConnectionAssociator
import com.c2v4.splendid.server.network.MainServer
import com.c2v4.splendid.server.network.ServerListener
import com.c2v4.splendid.server.network.login.ChallengeLogInService
import com.c2v4.splendid.server.network.login.SimpleGameService
import com.c2v4.splendid.server.network.login.SimpleLobbyService
import com.c2v4.splendid.server.network.util.LoggingListener
import org.apache.log4j.BasicConfigurator
import org.slf4j.LoggerFactory
import java.security.SecureRandom

/** Launches the server application.  */
object ServerLauncher {
    val LOG = LoggerFactory.getLogger(ServerLauncher::class.java)
    @JvmStatic fun main(args: Array<String>) {
        BasicConfigurator.configure()
        val associator = ConnectionAssociator()
        val gameService = SimpleGameService(associator,
                InitialDataLoader())
        MainServer(4, listOf(LoggingListener(), ServerListener(associator,
                ChallengeLogInService(SecureRandom()),
                SimpleLobbyService(gameService),gameService)))
    }
}