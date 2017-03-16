package com.c2v4.splendid.core.model

import com.c2v4.splendid.core.util.randomCard
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PlayerTest{

    @Test
    fun equalsTest(){
        val player1 = Player()
        val player2 = Player()
        assertThat(player1).isNotEqualTo(player2)
    }

    @Test
    fun containsInitializedCard(){
        val card = randomCard()
        val player = Player(listOf(card))
        assertThat(player.cards).contains(card)
    }
}