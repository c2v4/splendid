package com.c2v4.splendid.core.model

import com.c2v4.splendid.core.model.Resource.BLACK
import com.c2v4.splendid.core.model.Resource.RED
import junit.framework.Assert.assertTrue
import org.junit.Test

class NobleTest{

    @Test
    fun canBuyTest(){
        val noble = Noble(mapOf(RED to 3, BLACK to 3, Resource.BLUE to 3))
        val wallet = mapOf(RED to 3, BLACK to 3, Resource.BLUE to 3)
        assertTrue(noble.canBuy(wallet))
    }
}