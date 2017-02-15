package com.c2v4.splendid.core.model

import com.c2v4.splendid.core.model.Resource.GOLD
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

internal class DeckTest {

    private var deck: Deck<Card>? = null

    @Mock
    private val random: Random? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shuffleTest() {
        val cards = listOf(Card(0, 4, mapOf<Resource,Int>(), GOLD),
                Card(0, 1, mapOf<Resource,Int>(), GOLD),
                Card(0, 5, mapOf<Resource,Int>(), GOLD),
                Card(0, 2, mapOf<Resource,Int>(), GOLD),
                Card(0, 3, mapOf<Resource,Int>(), GOLD)
        )
        Mockito.`when`(random!!.nextInt(5)).thenReturn(1)
        Mockito.`when`(random.nextInt(4)).thenReturn(2)
        Mockito.`when`(random.nextInt(3)).thenReturn(2)
        Mockito.`when`(random.nextInt(2)).thenReturn(0)
        Mockito.`when`(random.nextInt(1)).thenReturn(0)
        deck = Deck.shuffledDeck(random, cards)

    }

    @Test
    fun createWithoutFirstTest() {
        val cards = listOf(Card(0, 4, mapOf<Resource,Int>(), GOLD),
                Card(0, 1, mapOf<Resource,Int>(), GOLD),
                Card(0, 5, mapOf<Resource,Int>(), GOLD),
                Card(0, 2, mapOf<Resource,Int>(), GOLD),
                Card(0, 3, mapOf<Resource,Int>(), GOLD)
        )
        Mockito.`when`(random!!.nextInt(5)).thenReturn(1)
        Mockito.`when`(random.nextInt(4)).thenReturn(2)
        Mockito.`when`(random.nextInt(3)).thenReturn(2)
        Mockito.`when`(random.nextInt(2)).thenReturn(0)
        Mockito.`when`(random.nextInt(1)).thenReturn(0)
        deck = Deck.shuffledDeck(random, cards)

        assertEquals(5, deck!!.size())
        assertEquals(deck!!.poll().map(Card::points).orElse(0), 1)
        assertEquals(4, deck!!.size())
        assertEquals(deck!!.poll().map(Card::points).orElse(0), 2)
        assertEquals(3, deck!!.size())
        assertEquals(deck!!.poll().map(Card::points).orElse(0), 3)
        assertEquals(2, deck!!.size())
        assertEquals(deck!!.poll().map(Card::points).orElse(0), 4)
        assertEquals(1, deck!!.size())
        assertEquals(deck!!.poll().map(Card::points).orElse(0), 5)
        assertEquals(0, deck!!.size())
        assertFalse(deck!!.poll().isPresent)
    }

}