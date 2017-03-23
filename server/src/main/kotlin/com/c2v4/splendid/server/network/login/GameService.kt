package com.c2v4.splendid.server.network.login

import com.c2v4.splendid.core.Game
import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Deck
import com.c2v4.splendid.core.model.Player
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.network.message.game.CardBuy
import com.c2v4.splendid.network.message.game.ReserveCard
import com.c2v4.splendid.network.message.game.ReservedCardBuy
import com.c2v4.splendid.network.message.game.TakeCoins
import com.c2v4.splendid.server.data.InitialDataLoader
import com.c2v4.splendid.server.network.ConnectionAssociator
import com.esotericsoftware.kryonet.Connection
import java.util.*

interface GameService {
    fun commenceGame(lobby: Set<String>)
    fun takeCoins(connection: Connection, received: TakeCoins)
    fun reserveCard(connection: Connection, received: ReserveCard)
    fun cardBuy(connection: Connection, received: CardBuy)
    fun reservedCardBuy(connection: Connection, received: ReservedCardBuy)

}

class SimpleGameService(val associator: ConnectionAssociator,
                        val initialDataLoader: InitialDataLoader) : GameService {

    val connectionsToGames = mutableMapOf<Connection, Game>()

    private val random = Random()

    override fun commenceGame(lobby: Set<String>) {

        val players = mutableListOf<Player>()
        val playersToConnections = mutableMapOf<Player, Connection>()

        lobby.forEach {
            val player = Player()
            players.add(player)
            playersToConnections.put(player, associator.getConnection(it))
            associator.associate(it, player)
        }

        val gameCoordinator = ServerGameCoordinator(playersToConnections, associator)

        gameCoordinator.sendStartGameEvent(lobby)

        val availableCoins = initialDataLoader.initialBank(players.size)
        gameCoordinator.sendInitialState(availableCoins)
        val game = Game(players,
                Board(listOf(Deck.shuffledDeck(random, initialDataLoader.initialCards[0]),
                        Deck.shuffledDeck(random, initialDataLoader.initialCards[1]),
                        Deck.shuffledDeck(random, initialDataLoader.initialCards[2])),
                        Deck.shuffledDeck(random, initialDataLoader.initialNobles),
                        players.size + 1, availableCoins), random,
                gameCoordinator)
        playersToConnections.values.forEach { connectionsToGames.put(it, game) }
        game.initializeBoard()
    }

    override fun takeCoins(connection: Connection, received: TakeCoins) {
        getGame(connection).takeCoins(associator.getPlayer(connection),
                received.taken,
                received.returned)
    }

    override fun reserveCard(connection: Connection, received: ReserveCard) {
        getGame(connection).reserveCard(associator.getPlayer(connection),
                received.tier,
                received.position,
                received.returned)
    }

    override fun cardBuy(connection: Connection, received: CardBuy) {
        getGame(connection).cardBuy(associator.getPlayer(connection),received.tier,received.position)
    }

    override fun reservedCardBuy(connection: Connection, received: ReservedCardBuy) {
        getGame(connection).reservedCardBuy(associator.getPlayer(connection),received.position)
    }

    private fun getGame(connection: Connection) = connectionsToGames[connection]!!

}
