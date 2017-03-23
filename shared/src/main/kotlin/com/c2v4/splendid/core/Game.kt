package com.c2v4.splendid.core

import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Noble
import com.c2v4.splendid.core.model.Noble.Companion.POINTS_FOR_NOBLE
import com.c2v4.splendid.core.model.Player
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.core.util.*
import com.c2v4.splendid.gateway.GameCoordinator
import java.util.*
import kotlin.collections.HashMap

class Game(players: List<Player>,
           val board: Board,
           random: Random,
           val gameCoordinator: GameCoordinator,
           currentPlayer: Player? = null) {
    val players: MutableList<Player> = players.toMutableList()
    // Just because compiler cannot understand that it will be set during construction
    var currentPlayer: Player = Player()

    init {
        if (currentPlayer == null) {
            setCurrentPlayer(random.nextInt(players.size))
        } else {
            this.currentPlayer = currentPlayer
        }
    }

    private fun setCurrentPlayer(ordinal: Int) {
        currentPlayer = this.players[ordinal]
        gameCoordinator.setCurrentPlayer(currentPlayer)
    }

    fun takeCoins(player: Player, taken: Map<Resource, Int>, returned: Map<Resource, Int>) {
        if (isCurrentPlayer(player) && isDrawCorrect(taken,
                returned,
                player.wallet) && board.availableCoins.haveValues(taken) && (taken.size == 3 || board.availableCoins.getOrElse(
                taken.keys.elementAt(0),
                { 0 }) > 3)) {
            player.wallet = player.wallet.merge(taken).subtractPositive(returned)
            board.availableCoins = board.availableCoins.merge(returned).subtractPositive(taken)
            gameCoordinator.coinsTaken(player, taken.subtract(returned), board.availableCoins)
            setNextCurrentPlayer()
        } else {
            throw IllegalArgumentException()
        }
    }

    private fun setNextCurrentPlayer() {
        setCurrentPlayer((players.indexOf(currentPlayer) + 1) % players.size)
    }

    private fun isCurrentPlayer(player: Player): Boolean = player == currentPlayer

    fun initializeBoard() {
        board.cardsOnBoard.forEachIndexed { tier, arrayOfCards ->
            arrayOfCards.forEachIndexed { position, card ->
                dealCard(position, tier)
            }
        }
        (0..players.size).forEach { i ->
            dealNoble(i)
        }
    }

    private fun dealNoble(position: Int) {
        val noble = board.dealNoble(position)
        noble.ifPresent { gameCoordinator.nobleDealt(position, it) }
    }

    private fun dealCard(position: Int, tier: Int) {
        val card = board.dealCard(tier, position)
        card.ifPresent { gameCoordinator.cardDealt(tier, position, it) }
    }

    fun reserveCard(player: Player, tier: Int, position: Int, returned: HashMap<Resource, Int>) {
        if (player.cardsReserved.count { it != null } > 2) throw IllegalStateException()
        val emptySpot = player.cardsReserved.indexOfFirst { it == null }
        player.cardsReserved[emptySpot] = board.cardsOnBoard[tier][position]
        val availableGold = board.availableCoins.getOrDefault(Resource.GOLD, 0)
        var returnedResource: Resource? = null
        if (availableGold > 0) {
            board.availableCoins = board.availableCoins.subtractPositive(mapOf(Resource.GOLD to 1))
            player.wallet = player.wallet.merge(mapOf(Resource.GOLD to 1))
            if (player.wallet.values.sum() > 10) {
                if (returned.size == 0) throw IllegalArgumentException()
                returnedResource = returned.keys.elementAt(0)
                player.wallet = player.wallet.subtractPositive(mapOf(returnedResource to 1))
                board.availableCoins = board.availableCoins.merge(mapOf(returnedResource to 1))
            }
        }
        gameCoordinator.cardReserved(player, emptySpot, tier, position, returnedResource)
        board.cardsOnBoard[tier][position] = null
        dealCard(position, tier)
        setNextCurrentPlayer()
    }

    fun cardBuy(player: Player, tier: Int, position: Int) {
        val card = board.cardsOnBoard[tier][position]!!
        if (!canBuy(card.cost, player.wallet.merge(player.resourcesFromCards))) {
            throw IllegalStateException()
        }
        val realCardCost = card.cost.subtract(player.resourcesFromCards).filter { it.value > 0 }
        val toPayWithGold = realCardCost.subtract(player.wallet).filter { it.value > 0 }.values.sum()
        val toPay = realCardCost.map {
            it.key to Math.min(it.value, player.wallet.getOrDefault(it.key, 0))
        }.toMap().merge(mapOf(Resource.GOLD to toPayWithGold)).filter { it.value != 0 }

        player.resourcesFromCards = player.resourcesFromCards.merge(mapOf(card.resource to 1))
        player.points += card.points
        player.wallet = player.wallet.subtractPositive(toPay)
        board.availableCoins = board.availableCoins.merge(toPay)
        board.cardsOnBoard[tier][position] = null
        gameCoordinator.cardBought(player, tier, position, toPay)

        dealCard(position, tier)

        val noblesGained = mutableListOf<Int>()
        board.noblesOnBoard.forEachIndexed { index, noble ->
            if (noble != null && canBuy(noble.resources, player.resourcesFromCards)) {
                noblesGained.add(index)
            }
        }

        noblesGained.forEach {
            player.points += POINTS_FOR_NOBLE
            board.noblesOnBoard[it] = null
            gameCoordinator.nobleTaken(player, it)
        }

        setNextCurrentPlayer()

    }

    fun reservedCardBuy(player: Player, position: Int) {

        val card = player.cardsReserved[position]!!
        if (!canBuy(card.cost, player.wallet.merge(player.resourcesFromCards))) {
            throw IllegalStateException()
        }
        val realCardCost = card.cost.subtract(player.resourcesFromCards).filter { it.value > 0 }
        val toPayWithGold = realCardCost.subtract(player.wallet).filter { it.value > 0 }.values.sum()
        val toPay = realCardCost.map {
            it.key to Math.min(it.value, player.wallet.getOrDefault(it.key, 0))
        }.toMap().merge(mapOf(Resource.GOLD to toPayWithGold)).filter { it.value != 0 }

        player.cardsReserved[position] = null
        player.resourcesFromCards = player.resourcesFromCards.merge(mapOf(card.resource to 1))
        player.points += card.points
        player.wallet = player.wallet.subtractPositive(toPay)
        board.availableCoins = board.availableCoins.merge(toPay)

        gameCoordinator.reservedCardBought(position, player, toPay)

        val noblesGained = mutableListOf<Int>()
        board.noblesOnBoard.forEachIndexed { index, noble ->
            if (noble != null && canBuy(noble.resources, player.resourcesFromCards)) {
                noblesGained.add(index)
            }
        }

        noblesGained.forEach {
            player.points += POINTS_FOR_NOBLE
            board.noblesOnBoard[it] = null
            gameCoordinator.nobleTaken(player, it)
        }

        setNextCurrentPlayer()
    }
}