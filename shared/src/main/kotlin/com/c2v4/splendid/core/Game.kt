package com.c2v4.splendid.core

import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Player
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.core.util.haveValues
import com.c2v4.splendid.core.util.isDrawCorrect
import com.c2v4.splendid.core.util.merge
import com.c2v4.splendid.core.util.subtractPositive
import com.c2v4.splendid.gateway.GameCoordinator
import java.util.*

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
            gameCoordinator.coinsTaken(player, taken)
            setNextCurrentPlayer()
        } else {
            throw IllegalArgumentException()
        }
    }

    private fun setNextCurrentPlayer() {
        currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
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
}