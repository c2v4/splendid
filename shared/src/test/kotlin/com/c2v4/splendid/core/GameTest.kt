package com.c2v4.splendid.core

import com.c2v4.splendid.core.model.Player
import com.c2v4.splendid.core.model.Resource.*
import com.c2v4.splendid.core.util.randomBoard
import com.c2v4.splendid.gateway.GameCoordinator
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

class GameTest {

    @Mock var random = Random()

    @Mock var coordinator: GameCoordinator? = null

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test fun drawPlayerTest() {
        val player1 = Player()
        val player2 = Player()
        val player3 = Player()
        val player4 = Player()
        val players = listOf(player1, player2, player3, player4)
        `when`(random.nextInt(players.size)).then { 2 }
        val game = Game(players, randomBoard(5), random, coordinator!!)
        assertThat(game.currentPlayer).isEqualTo(player3)
        verify(coordinator, com.nhaarman.mockito_kotlin.times(1))!!.setCurrentPlayer(player3)
    }

    @Test fun drawCoinsTest() {
        val player1 = Player()
        val player2 = Player()
        val player3 = Player()
        val player4 = Player()
        val players = listOf(player1, player2, player3, player4)
        val board = randomBoard(5)
        val availableCoins = mapOf(RED to 3, BLUE to 2, BLACK to 1)
        board.availableCoins = availableCoins
        val game = Game(players, board, random, coordinator!!)
        assertThat(game.currentPlayer).isEqualTo(player1)

        val taken = mapOf(RED to 1, BLUE to 1, BLACK to 1)
        game.takeCoins(player1, taken, mapOf())
        assertThat(game.currentPlayer).isEqualTo(player2)
        assertThat(board.availableCoins).containsAllEntriesOf(mapOf(RED to 2, BLUE to 1))
        assertThat(player1.wallet).containsAllEntriesOf(taken)
        verify(coordinator, com.nhaarman.mockito_kotlin.times(1))!!.coinsTaken(player1, taken)
    }


    @Test fun drawCoinsWithReturnTest() {
        val player1 = Player()
        val player2 = Player()
        val player3 = Player()
        val player4 = Player()
        val players = listOf(player1, player2, player3, player4)
        val board = randomBoard(5)
        player1.wallet = mapOf(GREEN to 9)
        val availableCoins = mapOf(RED to 3, BLUE to 2, BLACK to 1)
        board.availableCoins = availableCoins
        val game = Game(players, board, random, coordinator!!)
        assertThat(game.currentPlayer).isEqualTo(player1)

        val taken = mapOf(RED to 1, BLUE to 1, BLACK to 1)
        game.takeCoins(player1, taken, mapOf(GREEN to 1, BLUE to 1))
        assertThat(game.currentPlayer).isEqualTo(player2)
        assertThat(board.availableCoins).containsAllEntriesOf(mapOf(RED to 2, BLUE to 2,GREEN to 1))
        assertThat(player1.wallet).containsAllEntriesOf(mapOf(GREEN to 8, BLACK to 1, RED to 1))
        verify(coordinator, com.nhaarman.mockito_kotlin.times(1))!!.coinsTaken(player1, taken)
    }
}