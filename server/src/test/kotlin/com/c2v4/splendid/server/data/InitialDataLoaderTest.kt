package com.c2v4.splendid.server.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class InitialDataLoaderTest{

    private val initialDataLoader = InitialDataLoader()

    @Test
    fun nobleTest(){
        assertThat(initialDataLoader.initialNobles).hasSize(10)
        assertThat(initialDataLoader.initialNobles.toSet()).hasSize(10)
    }


    @Test
    fun cardTest(){
        assertThat(initialDataLoader.initialCards).hasSize(3)
        assertThat(initialDataLoader.initialCards[0]).hasSize(40)
        assertThat(initialDataLoader.initialCards[1]).hasSize(30)
        assertThat(initialDataLoader.initialCards[2]).hasSize(20)
        assertThat(initialDataLoader.initialCards.flatten().toSet()).hasSize(90)
    }
}